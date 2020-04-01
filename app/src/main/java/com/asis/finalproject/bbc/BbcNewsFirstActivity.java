package com.asis.finalproject.bbc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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
import com.asis.finalproject.R;
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
    private ArrayList<BbcArticles> newsArticleList;
    private SqliteDatabase mDatabase;
    private RecyclerView mRecyclerView;
    private BbcNewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue mRequestQueue;
    ImageButton favoriteBtn;
    private LinearLayout linearLayout;

    /**
     * Method where the activity is initialized
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbcnews_first_activity);
        /**
         * Call of the method for building the RecyclerView
         */
        buildRecyclerView();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsArticleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        /**
         *  Article search functionality step 1, using the edit text
         */

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

        linearLayout = findViewById(R.id.linearLayout);
        /**
         * Snackbar  and intent to move to favorites list
         */

        favoriteBtn = findViewById(R.id.favoriteBtn);
        if (favoriteBtn != null) {
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     * Transfers to the favorite page and calls for the SnackBar method
                     */
                    Intent goToFavorites = new Intent(BbcNewsFirstActivity.this, BbcNewsFavoriteActivity.class);
                    startActivityForResult(goToFavorites, REQUEST_RETURN_PAGE);
//                    startActivity(goToFavorites);
                    showSnackbar();
                }
            });
        }
        /**
         * Json parsing method call
         */
        parseJSON();

    }

    /**
     * removes items from the list
     * @param position
     */
    public void removeItem(int position) {
        newsArticleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }


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
                                newsArticleList.add(new BbcArticles(artTitle, artPubDate, description, url));
                            }

                            mAdapter = new BbcNewsAdapter(BbcNewsFirstActivity.this, newsArticleList);
                            mRecyclerView.setAdapter(mAdapter);
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
        ArrayList<BbcArticles> filteredList = new ArrayList<>();
        for (BbcArticles item : newsArticleList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

    /**
     * This method builds RecyslerView and is called in the onCreate section
     */
    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BbcNewsAdapter(this, newsArticleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Returns intent to the caller
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RETURN_PAGE) {
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
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
        builder.setTitle(getString(R.string.alert_title));
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton(R.string.alert_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = titleText.getText().toString();
                final String pubDate = pubDateText.getText().toString();
                final String description = descriptionText.getText().toString();
                final String webLink = webLinkText.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(pubDate) || TextUtils.isEmpty(description) || TextUtils.isEmpty(webLink)) {
                    Toast.makeText(BbcNewsFirstActivity.this, getString(R.string.alert_add_negative_toast), Toast.LENGTH_LONG).show();
                } else {
                    BbcArticles newArticle = new BbcArticles(title, pubDate, description, webLink);
                    mDatabase.addArticles(newArticle);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton((R.string.alert_negative_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BbcNewsFirstActivity.this, getString(R.string.alert_cancel_toast), Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

}