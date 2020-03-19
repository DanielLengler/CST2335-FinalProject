package com.asis.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.asis.finalproject.imageoftheday.ListOfImagesOfTheDay;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton bbcNewsReaderButton = findViewById(R.id.bbcNewsReaderButton);
        bbcNewsReaderButton.setOnClickListener((v) -> {

        });

        ImageButton guardianArticleSearchButton = findViewById(R.id.guardianArticleSearchButton);
        guardianArticleSearchButton.setOnClickListener((v) -> {

        });

        ImageButton nasaEarthImageButton = findViewById(R.id.nasaEarthImageButton);
        nasaEarthImageButton.setOnClickListener((v) -> {

        });

        ImageButton nasaImageOfDayButton = findViewById(R.id.nasaImageOfDayButton);
        Intent listOfImages = new Intent(MainActivity.this, ListOfImagesOfTheDay.class);
        nasaImageOfDayButton.setOnClickListener((v) -> {
            startActivity(listOfImages);
        });
        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbarMain);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navViewMain);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("Final Project");
        getSupportActionBar().setSubtitle("CST2335 - Version: 1.0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nav_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        Intent goToMain = new Intent(MainActivity.this, MainActivity.class);

        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.navBarBBC:
                startActivity(goToMain);
                break;
            case R.id.navBarTheGuardian:
                startActivity(goToMain);
                break;
            case R.id.navBarNasa:
                Intent goToEarthImagery = new Intent(MainActivity.this, NasaImageItem.class);
                startActivity(goToEarthImagery);
                break;
            case R.id.navBarNasaImageOfTheDay:
                Intent goToImageOfTheDay = new Intent(MainActivity.this, ListOfImagesOfTheDay.class);
                startActivity(goToImageOfTheDay);
                break;
            case R.id.help_item_main:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Help")
                        .setMessage("Here is how to use this application")
                        .setPositiveButton("Ok", ((dialog, which) -> {
                            dialog.cancel();
                        }))
                        .show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;
        Intent goToMain = new Intent(MainActivity.this, MainActivity.class);

        switch(item.getItemId())
        {
            case R.id.navDrawerBBC:
                startActivity(goToMain);
                break;
            case R.id.navDrawerTheGuardian:
                startActivity(goToMain);
                break;
            case R.id.navDrawerNasaEarth:
                Intent goToEarthImagery = new Intent(MainActivity.this, NasaImageItem.class);
                startActivity(goToEarthImagery);
                break;
            case R.id.navDrawerNasaImageOfTheDay:
                Intent goToImageOfTheDay = new Intent(MainActivity.this, ListOfImagesOfTheDay.class);
                startActivity(goToImageOfTheDay);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_main);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}