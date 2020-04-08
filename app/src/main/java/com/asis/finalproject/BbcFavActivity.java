package com.asis.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
     * @param savedInstanceState is a reference to a Bundle object that
     * is passed into the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_fav_list);

        favDB = new BbcFavDB(this);
        favRecyclerView = findViewById(R.id.recyclerView);
        favRecyclerView.setHasFixedSize(true);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        /**
         * Call for loading data from database
         */
        loadData();

        /**
         *  Article search functionality step 1, using the EditText
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
     * Loads data from database
     */
    private void loadData() {
        if (bbcFavItems != null){
            bbcFavItems.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try{
            while(cursor. moveToNext()){
                long id = cursor.getLong(cursor.getColumnIndex(BbcFavDB.COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_TITLE));
                String pubDate = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_PUBDATE));
                String description = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_DESCRIPTION));
                String webLink = cursor.getString(cursor.getColumnIndex(BbcFavDB.COL_URL));
                BbcFavItem favItem = new BbcFavItem(title, pubDate, description, webLink);
                favItem.setId((int)id);
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

    /**
     * Creates toolbar menu with different options
     * @param menu represents menu with different options (items)
     * @return menu options for selection
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Inflate the menu items for use in the action bar
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bbc_menu, menu);
        return true;
    }
    /**
     * This method allows to select items on the toolbar
     * @param item are represented as icons or located in dropdown menu
     * @return one of of the selections in the toolbar menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        /**
         * Switch statement for selecting different activities on the Toolbar
         * It provides also the toast messages
         */
        switch(item.getItemId())
        {
            case R.id.help_item:
                message = getString(R.string.help_toast);
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.help__menu_title))
                        .setMessage(getString(R.string.help_alert))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id){

                            }
                        })
                        .show();

                break;
            case R.id.mail:
                message = getString(R.string.mail_toast);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(getString(R.string.text_plain));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.sample_email)});
                intent.putExtra(Intent.EXTRA_SUBJECT, (getString(R.string.subj_here)));
                intent.putExtra(Intent.EXTRA_TEXT, (getString(R.string.body_text)));
                startActivity(intent);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }


}






