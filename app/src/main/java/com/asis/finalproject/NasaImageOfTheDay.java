package com.asis.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NasaImageOfTheDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_of_the_day);

//        Drawable d = null;
//        try {
//            URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
//            InputStream is = (InputStream) url.getContent();
//            d = Drawable.createFromStream(is, "src name");
//
//        } catch (Exception e) {
//
//        }
//
//        ImageView imageView = findViewById(R.id.imageView);
//        imageView.setImageDrawable(d);

        MyHTTPRequest req = new MyHTTPRequest();
        req.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=2020-02-01");  //Type 1
    }
    //Type1     Type2   Type3
    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
    {
        //Type3                Type1
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

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                String explanation = uvReport.getString("explanation");
                String date = uvReport.getString("date");
                String title = uvReport.getString("title");
                String imageUrl = uvReport.getString("url");

                Log.i("MainActivity", "Explanation: " + explanation);
                Log.i("MainActivity", "Explanation: " + date);
                Log.i("MainActivity", "Explanation: " + title);
                Log.i("MainActivity", "Explanation: " + imageUrl);

            }
            catch (Exception e)
            {
                Log.e("Exception", "EXCEPTION: " + e.getMessage());
            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {

        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);
        }
    }
}
