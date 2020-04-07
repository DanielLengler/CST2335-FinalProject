package com.asis.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * BbcNewsFirstActivity class
 * It is involved into proper appearance of the first page of BBCNews app
 */
public class BbcNewsFirstActivity extends AppCompatActivity {
    public static final int REQUEST_RETURN_PAGE = 500;
    private BbcAdapter bbcAdapter;
    private ArrayList<BbcItem> bbcItems = new ArrayList<>();
    private ArrayList<BbcFavItem> bbcFavItems = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private RequestQueue mRequestQueue;
    ImageButton favoriteBtn;
    private LinearLayout linearLayout;
    BbcFavDB favDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_first_activity);

        buildRecyclerView();

        mRequestQueue = Volley.newRequestQueue(this);
        linearLayout = findViewById(R.id.linearLayout);
        favDB = new BbcFavDB(this);
        bbcFavItems = favDB.listFavItems();


      //Search function part 1
        EditText edSearch = findViewById(R.id.searchEditText);
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



        // Snackbar  and intent to move to favorites list
        favoriteBtn = findViewById(R.id.favoriteBtn);
        if (favoriteBtn != null) {
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create an intent to go to the FavoritesActivity
                    Intent goToFavorites = new Intent(BbcNewsFirstActivity.this, BbcFavActivity.class);
                    startActivityForResult(goToFavorites, REQUEST_RETURN_PAGE);
                    showSnackbar();
                }
            });
        }

//        Button favBtn = findViewById(R.id.favBtn);
//        favBtn.setOnClickListener((view) -> {
//                addTaskDialog();
//        });




//
        /**
         * Json parsing method call
         */
        parseJSON();
/*
 //**********************************************
        bbcItems.add(new BbcItem(1, "title1", "pubDate1", "Description1","webLink1", "1"));
        bbcItems.add(new BbcItem(2, "title2", "pubDate2", "Description2","webLink2", "0"));
        bbcItems.add(new BbcItem(3, "title3", "pubDate3", "Description3","webLink3", "0"));
        bbcItems.add(new BbcItem(4, "title4", "pubDate4", "Description4","webLink4", "0"));
        bbcItems.add(new BbcItem(5, "title5", "pubDate5", "Description5","webLink5", "0"));
        bbcItems.add(new BbcItem(6, "title6", "pubDate6", "Description6","webLink6", "0"));
        bbcItems.add(new BbcItem(7, "title7", "pubDate7", "Description7","webLink7", "0"));
        bbcItems.add(new BbcItem(8, "title8", "pubDate8", "Description8","webLink8", "0"));
        bbcItems.add(new BbcItem(9, "title9", "pubDate9", "Description9","webLink9", "0"));
        bbcItems.add(new BbcItem(10, "title10", "pubDate10", "Description10","webLink10", "0"));

*/

 //***********************************
    }

    /**
     * Dialog window for adding article to the favorite list
     */
    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.bbc_article_list_layout, null);
        final TextView titleText = subView.findViewById(R.id.txtTitle);
        final TextView pubDateText = subView.findViewById(R.id.txtPubDate);
        final TextView descriptionText = subView.findViewById(R.id.txtDescription);
        final TextView webLinkText = subView.findViewById(R.id.link);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to add to favorites? ");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Add to Favorites", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = titleText.getText().toString();
                final String pubDate = pubDateText.getText().toString();
                final String description = descriptionText.getText().toString();
                final String webLink = webLinkText.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(pubDate) || TextUtils.isEmpty(description) || TextUtils.isEmpty(webLink)) {
                    Toast.makeText(BbcNewsFirstActivity.this, "Something went wrong. Check your article list", Toast.LENGTH_LONG).show();
                } else {
                    BbcFavItem newFavItem = new BbcFavItem(title, pubDate, description, webLink);
                    favDB.addArticles(newFavItem);
//                    favDB.updateArticles(new
//                            BbcItem(title, pubDate, description, webLink));
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BbcNewsFirstActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

//    public void addArticles(){
//        bbcItems.add(new BbcItem());
//        bbcAdapter.notifyDataSetChanged();
//    }

//    public void removeItem(int position) {
//        bbcItems.remove(position);
//       bbcAdapter.notifyItemRemoved(position);
//    }


//Snackbar message
public void showSnackbar() {
    Snackbar snackbar = Snackbar.make(linearLayout, getText(R.string.snackbar_message), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar1 = Snackbar.make(linearLayout, getText(R.string.undo_message), Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
            });
    snackbar.show();

}

    /**
     * JSON parsing using Volley library.
     */
    private void parseJSON(){
        /**
         * JSON link for pulling data from the Internet
         */
        String url = "https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Fworld%2Fus_and_canada%2Frss.xml";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            /**
                             * loops through the JSON array looking for the new items in the list
                             */
                            for (int i= 0; i < jsonArray.length(); i++){
                                JSONObject item = jsonArray.getJSONObject(i);
                                /**
                                 * When the "items" tag is found, it is mean the beginning
                                 * of the article. Then all the sub-items listed below are pulled.
                                 */
                                String artTitle = item.getString("title");
                                String artPubDate = item.getString("pubDate");
                                String description = item.getString("description");
                                String url = item.getString("link");
                                /**
                                 * new article is added to the list in the RecyclerView
                                 */
                                bbcItems.add(new BbcItem( artTitle, artPubDate, description, url));
                            }

                            bbcAdapter = new BbcAdapter( bbcItems, BbcNewsFirstActivity.this);
                            recyclerView.setAdapter(bbcAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    /**
     * Article search functionality step 2
     */
    private void filter(String text) {
        ArrayList<BbcItem> filteredList = new ArrayList<>();
        for (BbcItem item : bbcItems) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        bbcAdapter.filterList(filteredList);
    }

    /**
     * This method builds RecyslerView and is called in the onCreate section
     */
    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        bbcAdapter = new BbcAdapter(bbcItems, this);
        recyclerView.setAdapter(bbcAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RETURN_PAGE) {
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }



}