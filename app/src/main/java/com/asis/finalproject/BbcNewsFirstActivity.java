package com.asis.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * BbcNewsFirstActivity class
 * The first page of BBCNews app with loaded articles appear here
 */
public class BbcNewsFirstActivity extends AppCompatActivity {
    /**
     * this code is used for returning (results) to initial page
     */
    public static final int REQUEST_RETURN_PAGE = 500;
    /**
     * BBC news adapter for populating the views in each rows with
     * article details
     */
    private BbcAdapter bbcAdapter;
    /**
     * ArrayList of loaded articles from the Internet
     */
    private ArrayList<BbcItem> bbcItems = new ArrayList<>();
    /**
     * ArrayList of favorite list articles
     */
    private ArrayList<BbcFavItem> bbcFavItems = new ArrayList<>();
    /**
     * responsible for measuring and positioning item views within RecyclerView
     */
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * provides adapter-based view
     */
    private RecyclerView recyclerView;
//    private RequestQueue mRequestQueue;
    /**
     * button for switching between the pages:
     * (1)original page with articles and (2) favorite list
     */
    ImageButton favoriteBtn;
    /**
     * aligns all children in a single direction
     */
    private LinearLayout linearLayout;
    /**
     * Database of favorite articles
     */
    BbcFavDB favDB;
    /**
     * Progress bar declaration
     */
    private ProgressBar progressBar;

    /**
     *  Method where the activity is initialized
     * @param savedInstanceState is a reference to a Bundle object that
     *  is passed into the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_first_activity);

        /**
         * Call of the method for building the RecyclerView
         */
        buildRecyclerView();

        linearLayout = findViewById(R.id.linearLayout);
        /**
         * creation of new object of BbcFavDB database
         */
        favDB = new BbcFavDB(this);
        /**
         * assigning the favorite articles to Bbc favorite items
         */
        bbcFavItems = favDB.listFavItems();
        /**
         * This gets the toolbar from the layout
         */
        Toolbar tBar = findViewById(R.id.toolbar);
        /**
         * This loads the toolbar, which calls onCreateOptionsMenu below
         */
        setSupportActionBar(tBar);
        /**
         * This gets the progressbar from the layout and makes it visible
         */
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        /**
         *  Article search functionality step 1, using the EditText
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
         * SnackBar  and intent to move to favorites list
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
        content.execute("https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Fworld%2Fus_and_canada%2Frss.xml");

    }

    /**
     * Shows SnackBar message when click on favorites button
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
     * This method builds RecyclerView and is called in the onCreate section
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
    private class Content extends AsyncTask<String,Integer,String>{
        /**
         * This method uses JSON reading for loading articles from the Internet
         * @param params represents a result of the loading information from the Internet
         * @return the resulting statement
         */
        @Override
        protected String doInBackground(String... params) {
            publishProgress(0);
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream response = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                publishProgress(50);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String artTitle = item.getString("title");
                    String artPubDate = item.getString("pubDate");
                    String description = item.getString("description");
                    String linkUrl = item.getString("link");
                    bbcItems.add(new BbcItem(artTitle, artPubDate, description, linkUrl));
                }
                bbcAdapter = new BbcAdapter(bbcItems, BbcNewsFirstActivity.this);
                publishProgress(75);
//                recyclerView.setAdapter(bbcAdapter);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return "Finished task";
        }
        /**
         * This method is used for displaying the progress
         * @param values indicate the status of the progress bar, progress
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
        /**
         * The results of doInBackground are passed to this method
         * @param str represents a result of the doInBackground
         */
        @Override
        protected void onPostExecute(String str) {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setAdapter(bbcAdapter);
        }
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
        inflater.inflate(R.menu.menu_first, menu);
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

//

//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>() {
//                        /**
//                         * This method fetches required information from the internet
//                         * @param response provides response from the Internet
//                         */
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                JSONArray jsonArray = response.getJSONArray("items");
//                                for (int i= 0; i < jsonArray.length(); i++){
//                                    JSONObject item = jsonArray.getJSONObject(i);
//                                    String artTitle = item.getString("title");
//                                    String artPubDate = item.getString("pubDate");
//                                    String description = item.getString("description");
//                                    String url = item.getString("link");
//                                    bbcItems.add(new BbcItem( artTitle, artPubDate, description, url));
//                                }
//
//                                bbcAdapter = new BbcAdapter( bbcItems, BbcNewsFirstActivity.this);
//                                recyclerView.setAdapter(bbcAdapter);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            });
//
//            mRequestQueue.add(request);


