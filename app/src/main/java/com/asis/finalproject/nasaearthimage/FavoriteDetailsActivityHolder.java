package com.asis.finalproject.nasaearthimage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.asis.finalproject.R;

/**
 * A class that holds a {@link FavoriteDetails} fragment.
 * Is only used when the device is in portrait mode.
 */
public class FavoriteDetailsActivityHolder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_details_holder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.neid_toolbar_title);
            getSupportActionBar().setSubtitle(R.string.neid_toolbar_subtitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FavoriteDetails favoriteDetails = new FavoriteDetails();
        favoriteDetails.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, favoriteDetails)
                .commit();
    }
}
