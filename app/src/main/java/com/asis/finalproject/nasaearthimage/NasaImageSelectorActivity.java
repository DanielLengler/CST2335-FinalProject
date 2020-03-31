package com.asis.finalproject.nasaearthimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.asis.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

/**
 * Main launching page for the NASA earth imagery database part of the assignment.
 * Also referred to as NasaImageSelectorActivity and Nasa Image Finder on some occasions.
 */
public class NasaImageSelectorActivity extends AppCompatActivity {
    private static final String API_KEY = "AsIyU-yO5uSuTD7nKUi0R7GnLISjrJCCe3VRqGsd4wJk2vVp3sMLjAeY_qpuBo34";
    private TextView dateTextView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private NasaEarthImage nasaEarthImage = new NasaEarthImage();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_selector);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.neid_toolbar_title);
            getSupportActionBar().setSubtitle(R.string.neid_toolbar_subtitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageView = findViewById(R.id.earthImage);
        progressBar = findViewById(R.id.progressBar);
        dateTextView = findViewById(R.id.dateTextView);

        Button favoritesButton = findViewById(R.id.addToFavourites);
        favoritesButton.setOnClickListener(this::addToFavorites);

        Button searchButton = findViewById(R.id.findImageButton);
        searchButton.setOnClickListener(v -> {
            try {
                String longitude = ((EditText) findViewById(R.id.longitudeInput)).getText().toString();
                String latitude = ((EditText) findViewById(R.id.latitudeInput)).getText().toString();
                nasaEarthImage.setLatitude(Double.parseDouble(latitude));
                nasaEarthImage.setLongitude(Double.parseDouble(longitude));
                NasaImageQuery nasaImageQuery = new NasaImageQuery();
                nasaImageQuery.execute(String.format(" http://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/%s,%s/20?dir=180&ms=500,500&key="+API_KEY,
                        latitude, longitude));
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.bad_input_message_neid, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Code that adds the nasaEarthImage, updates its id and downloads the image to local storage
     *
     * @param v - pass through variable
     */
    private void addToFavorites(View v) {
        if (nasaEarthImage.getPath() == null) {
            Snackbar.make(v, R.string.null_image_path_neid, Snackbar.LENGTH_LONG).show();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
        long id = databaseHelper.insertImage(nasaEarthImage);
        nasaEarthImage.setId(id);
        nasaEarthImage.setPath(id + ".png");
        databaseHelper.update(nasaEarthImage);
        databaseHelper.close();

        try (FileOutputStream out = openFileOutput(nasaEarthImage.getPath(), Context.MODE_PRIVATE)) {
            ((BitmapDrawable) imageView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
            Snackbar.make(v, R.string.added_to_favorites_neid, Snackbar.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inflates the menu with a custom xml menu
     *
     * @param menu - the menu to inflate
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nasa_earth_image_menu, menu);
        return true;
    }

    /**
     * Handles the click actions for the toolbar menu
     *
     * @param menuItem - the item that was clicked
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.actionHelp:
                SharedPreferences sharedPreferences = this.getSharedPreferences("com.asis.finalproject", MODE_PRIVATE);
                int timesOpened = sharedPreferences.getInt("OPENED-TIMES", 0) + 1;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("OPENED-TIMES", timesOpened);
                editor.apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(NasaImageSelectorActivity.this);
                builder.setTitle(R.string.help_neid)
                        .setMessage(getString(R.string.info_message1_neid) +
                                "\n " + getString(R.string.info_message2_neid) + " " + timesOpened + " " + getString(R.string.info_message3_neid))
                        .setPositiveButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.actionFavorites:
                startActivity(new Intent(this, NasaImageFavoritesActivity.class));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * Class that talks to ad gets data from a json rest api.
     */
    @SuppressLint("StaticFieldLeak")
    private class NasaImageQuery extends AsyncTask<String, Integer, String> {

        private Bitmap image;
        private String errorMessage = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(0);
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) sb.append(line).append("\n");
                String result = sb.toString();
                publishProgress(30);
                JSONObject nasaImageJson = new JSONObject(result);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTime(Objects.requireNonNull(sdf.parse(nasaImageJson.getString("date"))));

                nasaEarthImage.setDate(calendar);

                String imageURL = nasaImageJson.getString("url");
                publishProgress(70);

                image = null;
                URL imageUrl = new URL(imageURL);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }
                nasaEarthImage.setPath("imageFound");

                publishProgress(100);
            } catch (IOException | JSONException | ParseException | NullPointerException e) {
                errorMessage = getString(R.string.null_image_path_neid);
                return errorMessage;
            }

            return getString(R.string.finished_neid);
        }

        /**
         * Sets visual elements if no errors have occurred.
         *
         * @param s - the end message
         */
        @Override
        protected void onPostExecute(String s) {
            if (errorMessage == null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                imageView.setImageBitmap(image);
                dateTextView.setText(format.format(nasaEarthImage.getDate().getTime()));
            } else {
                Toast.makeText(NasaImageSelectorActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }

        /**
         * Updates the progress bar's value
         *
         * @param values - the progress percentage
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
    }
}
