package com.asis.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/**
 * BbcNewsFavoriteActivity class
 * It is involved into proper appearance of the favorites page of BBCNews app
 */
public class BbcFavActivity extends AppCompatActivity {

    ArrayList<BbcFavItem> bbcFavItems = new ArrayList<>();
    private BbcFavDB favDB;
    ImageButton returnBtn;
    private BbcFavAdapter favAdapter;
    private RecyclerView favRecyclerView;

    /**
     * Method where the activity is initialized
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbcnews_favorite_list_activity);

        favDB = new BbcFavDB(this);
        favRecyclerView = findViewById(R.id.recyclerView);
//        favRecyclerView.setHasFixedSize(true);
//        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        loadData();

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
/*
//        bbcFavItems = new ArrayList<>();
        bbcFavItems.add(new BbcFavItem("title1", "pubDate2", "description1", "webLink1"));
        bbcFavItems.add(new BbcFavItem("title2", "pubDate3", "description2", "webLink2"));
        bbcFavItems.add(new BbcFavItem("title3", "pubDate4", "description3", "webLink3"));
        bbcFavItems.add(new BbcFavItem("title4", "pubDate5", "description4", "webLink4"));
        bbcFavItems.add(new BbcFavItem("title5", "pubDate6", "description5", "webLink5"));
        bbcFavItems.add(new BbcFavItem("title6", "pubDate7", "description6", "webLink6"));
        bbcFavItems.add(new BbcFavItem("title7", "pubDate8", "description7", "webLink7"));
        bbcFavItems.add(new BbcFavItem("title8", "pubDate7", "description6", "webLink6"));
        bbcFavItems.add(new BbcFavItem("title9", "pubDate8", "description8", "webLink8"));
        bbcFavItems.add(new BbcFavItem("title5", "pubDate6", "description5", "webLink5"));
*/

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
////        favRecyclerView.setLayoutManager(linearLayoutManager);
//
//        favDB =new BbcFavDB(this);
//        bbcFavItems = favDB.listFavItems();
//        favRecyclerView.setAdapter(new BbcFavAdapter(bbcFavItems, this));
//        favAdapter.notifyDataSetChanged();
    }
    private void loadData() {
        if (bbcFavItems != null){
            bbcFavItems.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try{
            while(cursor. moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_TITLE));
                String pubDate = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_PUBDATE));
                String description = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_DESCRIPTION));
                String webLink = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_URL));
//                int id = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_ID));
                BbcFavItem favItem = new BbcFavItem(title, pubDate, description, webLink);
                bbcFavItems.add(favItem);
            }
        }finally {
            if(cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
        favAdapter = new BbcFavAdapter(bbcFavItems,this);
        favRecyclerView.setAdapter(favAdapter);
    }
    /**
     * Article search functionality step 2
     */
    private void filter(String text){
        ArrayList<BbcFavItem> filteredList = new ArrayList<>();
        for (BbcFavItem item : bbcFavItems){
            if (item.getFav_title().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        favAdapter.filterList(filteredList);
    }



}






