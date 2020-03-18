package com.asis.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.asis.finalproject.nasaearthimage.NasaImageSelectorActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBarAndDrawer();

        ImageButton bbcNewsReaderButton = findViewById(R.id.bbcNewsReaderButton);
        bbcNewsReaderButton.setOnClickListener((v) -> launchBBC());

        ImageButton guardianArticleSearchButton = findViewById(R.id.guardianArticleSearchButton);
        guardianArticleSearchButton.setOnClickListener((v) -> launchGuardian());

        ImageButton nasaEarthImageButton = findViewById(R.id.nasaEarthImageButton);
        nasaEarthImageButton.setOnClickListener((v) -> launchNasaEarthImage());

        ImageButton nasaImageOfDayButton = findViewById(R.id.nasaImageOfDayButton);
        nasaImageOfDayButton.setOnClickListener((v) -> launchNasaImageOfTheDay());
    }

    private void setupActionBarAndDrawer() {
        //For ToolBar:
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //For NavigationDrawer:
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.actionLaunchBBC:
                launchBBC();
                break;
            case R.id.actionLaunchGuardian:
                launchGuardian();
                break;
            case R.id.actionLaunchNasaEarthImage:
                launchNasaEarthImage();
                break;
            case R.id.actionLaunchNasaImageOfDay:
                launchNasaImageOfTheDay();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionLaunchBBC:
                launchBBC();
                break;
            case R.id.actionLaunchGuardian:
                launchGuardian();
                break;
            case R.id.actionLaunchNasaEarthImage:
                launchNasaEarthImage();
                break;
            case R.id.actionLaunchNasaImageOfDay:
                launchNasaImageOfTheDay();
                break;
        }
        return true;
    }

    /*
    * Above this line is code for the navigation drawer and toolbars.
    * You should not need to touch any of it.
    * --------------------------------------------------------------
    * In the methods below go your startActivity code for launching your
    * particular part of the project.
    * */

    /**
     * Starts the BBC News activity
     */
    private void launchBBC() {

    }

    /**
     * Starts the Guardian News activity
     */
    private void launchGuardian() {

    }

    /**
     * Starts the Nasa earth image activity
     */
    private void launchNasaEarthImage() {
        startActivity(new Intent(this, NasaImageSelectorActivity.class));
    }

    /**
     * Starts the Nasa image of the day activity
     */
    private void launchNasaImageOfTheDay() {

    }
}
