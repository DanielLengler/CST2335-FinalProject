package com.asis.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.bbc.FirstActivity;
import com.asis.finalproject.guardian.GuardianSearchBar;
import com.asis.finalproject.nasaearthimage.NasaImageSelectorActivity;
import com.asis.finalproject.nasaimageoftheday.ListOfImages;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton bbcNewsReaderButton = findViewById(R.id.bbcNewsReaderButton);
        bbcNewsReaderButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, FirstActivity.class)));

        ImageButton guardianArticleSearchButton = findViewById(R.id.guardianArticleSearchButton);
        guardianArticleSearchButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, GuardianSearchBar.class);
            startActivity(intent);
        });

        ImageButton nasaEarthImageButton = findViewById(R.id.nasaEarthImageButton);
        nasaEarthImageButton.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, NasaImageSelectorActivity.class)));

        ImageButton nasaImageOfDayButton = findViewById(R.id.nasaImageOfDayButton);
        Intent listOfImages = new Intent(MainActivity.this, ListOfImages.class);
        nasaImageOfDayButton.setOnClickListener((v) -> startActivity(listOfImages));
    }
}
