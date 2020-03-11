package com.asis.finalproject.nasaearthimage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.R;

public class FavoriteDetailsActivityHolder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_details_holder);

        FavoriteDetails favoriteDetails = new FavoriteDetails();
        favoriteDetails.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, favoriteDetails)
                .commit();
    }
}
