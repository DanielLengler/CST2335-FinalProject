package com.asis.finalproject.imageoftheday;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.R;

public class EmptyActivityImageOfTheDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_image_of_the_day);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
        DetailsFragmentImageOfTheDay dFragment = new DetailsFragmentImageOfTheDay();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocationImageOfTheDay, dFragment)
                .commit();
    }
}
