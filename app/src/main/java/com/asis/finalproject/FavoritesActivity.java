package com.asis.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    ArrayList<BbcArticles> allArticles;
    private SqliteDatabase mDatabase;
    private BbcNewsFavoriteAdapter mAdapter;
    ImageButton returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        // Search functionality step1
        EditText edSearch = findViewById(R.id.searchEditText2);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        // receive an intent from the previous  page
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);

        //return to the previous page
        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView favArticleView = findViewById(R.id.recyclerFavorites);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        favArticleView.setLayoutManager(linearLayoutManager);
        favArticleView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allArticles = mDatabase.listArticles();
        if (allArticles.size() > 0) {
            favArticleView.setVisibility(View.VISIBLE);
            mAdapter = new BbcNewsFavoriteAdapter(this, allArticles);
            favArticleView.setAdapter(mAdapter);
        }
        else {
            favArticleView.setVisibility(View.GONE);
            Toast.makeText(this, "There are no saved articles in Favorites list. Start adding now", Toast.LENGTH_LONG).show();
        }

    }
    //search functionality step2
    private void filter(String text){
        ArrayList<BbcArticles> filteredList = new ArrayList<>();
        for (BbcArticles item : allArticles){
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

}
