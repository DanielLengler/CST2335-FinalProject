package com.asis.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    private ProgressBar progressBar;

    /**
     *  Method where the activity is initialized
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_first_activity);

        /**
         * Call of the method for building the RecyclerView
         */
        buildRecyclerView();

        mRequestQueue = Volley.newRequestQueue(this);
        linearLayout = findViewById(R.id.linearLayout);
        favDB = new BbcFavDB(this);
        bbcFavItems = favDB.listFavItems();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

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

        /**
         * Snackbar  and intent to move to favorites list
         */
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

        /**
         * Call of the execute method for starting loading information from the Internet
         */
        Content content = new Content();
        content.execute();

    }


//    public void addArticles(){
//        bbcItems.add(new BbcItem());
//        bbcAdapter.notifyDataSetChanged();
//    }

//    public void removeItem(int position) {
//        bbcItems.remove(position);
//       bbcAdapter.notifyItemRemoved(position);
//    }

    /**
     * Snackbar message when clicked on favorites button
     */

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

    /**
     * This method returns the result back to the inquiry point
     * @param requestCode should coincide with the one initiated at the beginning of the intent
     * @param resultCode result code - is a default value specifying the status of activity
     * @param data is optional parameter, especially when there is just return to the previous page
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
     * This inner class is a subclass of AsyncTask
     */
    private class Content extends AsyncTask<Void, Void,Void>{
        /**
         * This method uses JSON reading for loading articles from the Internet
         * @param voids represents a result of the loading information from the Internet
         * @return null in case if there is no Internet connection
         */
        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Fworld%2Fus_and_canada%2Frss.xml";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        /**
                         * This method fetches required information from the internet
                         * @param response provides response from the Internet
                         */
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("items");
                                for (int i= 0; i < jsonArray.length(); i++){
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    String artTitle = item.getString("title");
                                    String artPubDate = item.getString("pubDate");
                                    String description = item.getString("description");
                                    String url = item.getString("link");
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

            return null;
        }

        /**
         * This method is used for displaying the progress
         * @param values indicate the status of the progress bar, progress
         */
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        /**
         * The results of doInBackground are passed to this method
         * @param aVoid represents a result of the doInBackground
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}