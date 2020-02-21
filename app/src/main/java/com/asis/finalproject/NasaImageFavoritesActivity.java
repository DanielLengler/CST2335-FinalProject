package com.asis.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NasaImageFavoritesActivity extends AppCompatActivity {

    private ArrayList<NasaEarthImage> nasaEarthImages = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_favorites);

        loadFavoritesList();

        favoritesAdapter = new FavoritesAdapter(this, R.layout.nasa_earth_image_row, nasaEarthImages);
        Log.e("onCreate", String.valueOf(nasaEarthImages.size()));

        ListView listView = findViewById(R.id.favouritesList);
        listView.setAdapter(favoritesAdapter);
    }

    private void loadFavoritesList(){
        //Test data
        nasaEarthImages.add(new NasaEarthImage(123.2, 456.1, null));
        nasaEarthImages.add(new NasaEarthImage(123.2, 456.1, null));
    }

    private class FavoritesAdapter extends ArrayAdapter<NasaEarthImage> {

        FavoritesAdapter(@NonNull Context context, int resource, List<NasaEarthImage> list) {
            super(context, resource, list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = convertView;

            if(rowView == null && layoutInflater != null) {
                rowView = layoutInflater.inflate(R.layout.nasa_earth_image_row, parent, false);
            }

            TextView longitudeTextView = rowView.findViewById(R.id.longitude);
            TextView latitudeTextView = rowView.findViewById(R.id.latitude);
            ImageView imageView = rowView.findViewById(R.id.imageView);

            NasaEarthImage nasaEarthImage = getItem(position);
            Log.e("FavoritesAdapter", String.valueOf(nasaEarthImage));
            if (nasaEarthImage != null) {
                longitudeTextView.setText(String.valueOf(nasaEarthImage.getLongitude()));
                latitudeTextView.setText(String.valueOf(nasaEarthImage.getLatitude()));
                imageView.setImageBitmap(nasaEarthImage.getImage());
            }

            return rowView;
        }
    }
}
