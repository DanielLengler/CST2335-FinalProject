package com.asis.finalproject.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asis.finalproject.R;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.widget.Toast.LENGTH_LONG;

/**
 * This class is responsible for load and process layouts that shows image of the day.
 * The class download data from URL and store into the phone.
 * After that it call the methods to set values into layout
 */
public class NasaImageOfTheDay extends AppCompatActivity {

    SQLiteDatabase db; // Used to initialize database.
    String nasaImageExplanation; // Text describing the image from NASA.
    String nasaImageDate; // Date that the image was added to the API.
    String nasaImageTitle; // Title of the image.
    String nasaImageUrl; // URL from the image.

    /**
     *
     * @param savedInstanceState Saved Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_of_the_day);

        // Brings date left from previous execution.
        Intent fromMain = getIntent();
        String dateFromMain =  fromMain.getStringExtra("DATE");

        // Create button and set listener to save data from Nasa to DB.
        Button saveImage = findViewById(R.id.buttonSaveNasaImageOfTheDay);
        saveImage.setOnClickListener( click ->
        {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(DbOpenerImageOfTheDay.COL_DATE, nasaImageDate);
            newRowValues.put(DbOpenerImageOfTheDay.COL_EXPLANATION, nasaImageExplanation);
            newRowValues.put(DbOpenerImageOfTheDay.COL_URL, nasaImageUrl);
            newRowValues.put(DbOpenerImageOfTheDay.COL_TITLE, nasaImageTitle);
            newRowValues.put(DbOpenerImageOfTheDay.COL_PATH, "F");
            DbOpenerImageOfTheDay dbOpener = new DbOpenerImageOfTheDay(this);
            db = dbOpener.getWritableDatabase();
            long newId = db.insert(DbOpenerImageOfTheDay.TABLE_NAME, null, newRowValues);

            Toast.makeText(NasaImageOfTheDay.this, "Image saved to the database", LENGTH_LONG).show();
            finish();
        });

        // Call class to download passing Nasa URL
        MyHTTPRequest req = new MyHTTPRequest();
        req.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + dateFromMain);  //Type 1
    }

    //Type1     Type2   Type3
    /**
     * Class responsible to handle data from Nasa. Initialize and call methods to retrieve and store data.
     */
    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
    {
        /**
         * The following views are initialized with items from view.
         * After this initialization, the same variables are called to set the values downloaded.
         */
        ImageView nasaImage = findViewById(R.id.imageViewNasaImageOfTheDay);
        TextView imageTitleView = findViewById(R.id.textViewTitleNasaImageOfTheDay);
        TextView imageExplanationView = findViewById(R.id.textViewExplanationNasaImageOfTheDay);
        TextView imageDateView = findViewById(R.id.textViewDateNasaImageOfTheDay);
        TextView imageUrlView = findViewById(R.id.textViewNasaUrlImageOfTheDay);
        ProgressBar loadingImage = findViewById(R.id.progressBarImageOfTheDay);
        Bitmap image = null;

        //Type3                Type1
        /**
         *
         * @param args
         * this parameter carry an array of url to be used to download data from source
         * @return String indicating if code executed or not
         *
         * In this method, data is download and stored in the database and an image is saved into the phone.
         */
        public String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject jsonObject = new JSONObject(result);

                //get the double associated with "value"
                nasaImageExplanation = jsonObject.getString("explanation");
                publishProgress(20);
                nasaImageDate = jsonObject.getString("date");
                publishProgress(40);
                nasaImageTitle = jsonObject.getString("title");
                publishProgress(60);
                nasaImageUrl = jsonObject.getString("url");
                publishProgress(80);

                    // Next session download the image and store into the phone
                    URL imageUrl = new URL(nasaImageUrl);
                    HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        Log.i("Creating Bitmap", "Response Code: " + responseCode);
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                    }

                    // Check if image exist in the phone
                    Log.i("FileName", "File Name: " + nasaImageDate + ".png");
                    if(fileExistance(nasaImageDate + ".png")){
                        Log.i("FileExists", "The file already exists");
                        FileInputStream fis = null;
                        try {    fis = openFileInput(nasaImageDate + ".png");   }
                        catch (FileNotFoundException e) {    e.printStackTrace();  }
                        Bitmap bm = BitmapFactory.decodeStream(fis);
                        publishProgress(100);

                    } else {
                        Log.i("FileNotExists", "The file is being downloaded");
                        FileOutputStream outputStream = openFileOutput( nasaImageDate + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        publishProgress(100);
                    }

            }
            catch (Exception e)
            {
                Log.e("Exception", "EXCEPTION: " + e.getMessage());
            }

            return "Done";
        }

        //Type 2

        /**
         * Method loads progress bar. Receives the percentage and Set visibility with the percentage loaded.
         * @param args Carry a number that represents a percentage for the progress bar.
         */
        public void onProgressUpdate(Integer ... args)
        {
            loadingImage.setVisibility(View.VISIBLE);
            loadingImage.setProgress(args[0]);
            Log.i("onProgressUpdate", "Progress Update");
        }

        //Type3

        /**
         * This method set the parameters downloaded to the layout items.
         * @param fromDoInBackground Message passed from doInBackground method
         */
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);
            nasaImage.setImageBitmap(image);
            imageTitleView.setText(String.format("%s%s", getString(R.string.imageTitleViewImageOfTheDay), nasaImageTitle));
            imageExplanationView.setText(String.format("%s%s", getString(R.string.explanationImageOfTheDay), nasaImageExplanation));
            imageDateView.setText(String.format("%s%s", getString(R.string.imageDateViewImageOfTheDay), nasaImageDate));
            imageUrlView.setText(String.format("%s%s", getString(R.string.imageUrlViewImageOfTheDay), nasaImageUrl));
            loadingImage.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     * @param fname This is the name of the image in the phone.
     * @return boolean true if file exist and false otherwise
     */
    public boolean fileExistance(String fname){
        Log.i("fileExistance", "Inside fileExistance Method");
        File file = getBaseContext().getFileStreamPath(fname);
        Log.i("filePath", getFileStreamPath(fname).toString());
        return file.exists();
    }
}