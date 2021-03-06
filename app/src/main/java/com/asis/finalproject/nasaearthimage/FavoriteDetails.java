package com.asis.finalproject.nasaearthimage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asis.finalproject.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteDetails extends Fragment {

    /**
     * Required empty public constructor
     */
    public FavoriteDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_details, container, false);
        NasaEarthImage nasaEarthImage = (NasaEarthImage) getArguments().getSerializable("NASA-IMAGE");

        if(nasaEarthImage != null) {
            TextView longitudeTV = view.findViewById(R.id.longitude);
            longitudeTV.setText(String.valueOf(nasaEarthImage.getLongitude()));

            TextView latitudeTV = view.findViewById(R.id.latitude);
            latitudeTV.setText(String.valueOf(nasaEarthImage.getLatitude()));

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageBitmap(NasaImageFavoritesActivity.loadImageFromPath(view.getContext(), nasaEarthImage));
        }


        return view;
    }
}
