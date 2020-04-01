package com.asis.finalproject.bbc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.R;

import java.util.ArrayList;
/**
 * BbcNewsFavoriteActivity class
 * It is involved into proper appearance of the favorites page of BBCNews app
 */
public class BbcNewsFavoriteActivity extends AppCompatActivity {
    ArrayList<BbcArticles> allArticles;
    private SqliteDatabase mDatabase;
    private BbcNewsFavoriteAdapter mAdapter;
    ImageButton returnBtn;

    /**
     * Method where the activity is initialized
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbcnews_favorite_list_activity);

        /**
         *  Article search functionality step 1, using the edit text
         */
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

        /**
         *  Receives an intent from the previous  page
         */

        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);

        /**
         * Returns to the previous page with the click on Return button
         */
        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    /**
     * Article search functionality step 2
     */
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
