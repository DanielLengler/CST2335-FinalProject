package com.asis.finalproject.guardian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.R;

public class GuardianSearchBar extends AppCompatActivity {

    public static final String SEARCH = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_searchbar);

        EditText search = findViewById(R.id.guardianSearchBar);
        Button toResults = findViewById(R.id.guardianSearchCriteriaButton);

        toResults.setOnClickListener((v)->{
            Intent nextActivity = new Intent(GuardianSearchBar.this, GuardianSearch.class);
            nextActivity.putExtra(SEARCH, search.getText().toString());
            startActivity(nextActivity);
        });
    }
}
