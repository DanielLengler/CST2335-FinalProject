package com.asis.finalproject.nasaearthimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import javax.net.ssl.HttpsURLConnection;

public class NasaImageSelectorActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private NasaEarthImage nasaEarthImage = new NasaEarthImage();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_selector);

        imageView = findViewById(R.id.earthImage);
        progressBar = findViewById(R.id.progressBar);

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
                nasaImageQuery.execute(String.format("https://api.nasa.gov/planetary/earth/imagery/?lon=%s&lat=%s&date=2014-02-01&api_key=DEMO_KEY",
                        longitude, latitude));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Unable to parse you're inputted longitude or latitude", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addToFavorites(View v) {
        if(imageView.getDrawable() == null) {
            Snackbar.make(v, "No image found", Snackbar.LENGTH_LONG).show();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
        long id = databaseHelper.insertImage(nasaEarthImage);
        nasaEarthImage.setId(id);
        databaseHelper.close();

        try (FileOutputStream out = openFileOutput(nasaEarthImage.getPath(), Context.MODE_PRIVATE)) {
            ((BitmapDrawable) imageView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, NasaImageFavoritesActivity.class));
    }

    private class NasaImageQuery extends AsyncTask<String, Integer, String> {

        private Bitmap image;
        private String errorMessage = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(0);
                URL url = new URL(strings[0]);

                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) sb.append(line).append("\n");
                String result = sb.toString();
                JSONObject nasaImageJson = new JSONObject(result);

                String imageURL = nasaImageJson.getString("url");

                image = null;
                URL imageUrl = new URL(imageURL);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }
                nasaEarthImage.setPath(nasaEarthImage.getId() + ".png");

                publishProgress(100);


            } catch (IOException | JSONException e) {
                Log.e("NasaImageQuery-error", e.getMessage());
                errorMessage = "No image was found";
                return errorMessage;
            }

            return "Finished";
        }

        @Override
        protected void onPostExecute(String s) {
            if(errorMessage == null) {
                imageView.setImageBitmap(image);
            } else {
                Toast.makeText(NasaImageSelectorActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
    }


}
