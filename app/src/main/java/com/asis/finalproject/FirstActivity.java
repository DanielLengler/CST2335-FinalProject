package com.asis.finalproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class FirstActivity extends AppCompatActivity {
    public static final int REQUEST_RETURN_PAGE = 500;
    ArrayList<BbcArticles> allArticles;
    private SqliteDatabase mDatabase;
    private BbcNewsAdapter mAdapter;
    private LinearLayout linearLayout;
    private RequestQueue mRequestQueue;
    ProgressBar progressBar;
    RecyclerView articleView;
    ImageButton favoriteBtn;
    LinearLayoutManager linearLayoutManager;
    private String NEWS_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        linearLayout = findViewById(R.id.linearLayout);

        //This gets the toolbar from the layout:
        Toolbar tBar = findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        //  setSupportActionBar(tBar);

        progressBar =  findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);


//        MyHTTPRequest req = new MyHTTPRequest();
//        req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

        // Search functionality step 1
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
//        //This gets the toolbar from the layout:
        Toolbar toolbar = findViewById(R.id.toolbar);
        //This loads the toolbar, which calls onCreateOptionsMenu below:
//        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        buildRecyclerView();

        // Snackbar  and intent to move to favorites list
        favoriteBtn = findViewById(R.id.favoriteBtn);
        if (favoriteBtn != null) {
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create an intent to go to the FavoritesActivity
                    Intent goToFavorites = new Intent(FirstActivity.this, FavoritesActivity.class);
                    startActivityForResult(goToFavorites, REQUEST_RETURN_PAGE);
                    showSnackbar();
                }
            });
        }
        //tentative button for adding the articles to favorites
        ImageButton btnAdd = findViewById(R.id.addBtn);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });


            // placeHolders for articles
        allArticles.add(new BbcArticles("title1", "pubDate2", "description1", "webLink1"));
        allArticles.add(new BbcArticles("title2", "pubDate3", "description2", "webLink2"));
        allArticles.add(new BbcArticles("title3", "pubDate4", "description3", "webLink3"));
        allArticles.add(new BbcArticles("title4", "pubDate5", "description4", "webLink4"));
        allArticles.add(new BbcArticles("title5", "pubDate6", "description5", "webLink5"));
        allArticles.add(new BbcArticles("title6", "pubDate7", "description6", "webLink6"));
        allArticles.add(new BbcArticles("title7", "pubDate8", "description7", "webLink7"));
        allArticles.add(new BbcArticles("title8", "pubDate7", "description6", "webLink6"));
        allArticles.add(new BbcArticles("title9", "pubDate8", "description8", "webLink8"));
        allArticles.add(new BbcArticles("title5", "pubDate6", "description5", "webLink5"));

    }

    //Snackbar message
    public void showSnackbar() {
        Snackbar snackbar = Snackbar.make(linearLayout, getText(R.string.snackbar_message), Snackbar.LENGTH_INDEFINITE)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar1 = Snackbar.make(linearLayout, getText(R.string.undo_message), Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                });
        snackbar.show();
    }
    // search functionality step 2
    private void filter(String text) {
        ArrayList<BbcArticles> filteredList = new ArrayList<>();
        for (BbcArticles item : allArticles) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.bbc_article_list_layout, null);
        final TextView titleText = subView.findViewById(R.id.txtTitle);
        final TextView pubDateText = subView.findViewById(R.id.txtPubDate);
        final TextView descriptionText = subView.findViewById(R.id.txtDescription);
        final TextView webLinkText = subView.findViewById(R.id.link);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add to Favorites");
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
                    Toast.makeText(FirstActivity.this, "Something went wrong. Check your acticle list", Toast.LENGTH_LONG).show();
                } else {
                    BbcArticles newArticle = new BbcArticles(title, pubDate, description, webLink);
                    mDatabase.addArticles(newArticle);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FirstActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    //return intent to the caller
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RETURN_PAGE) {
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
/*
    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {
        String title, pubDate, description, webUrl;
        ProgressBar progressBar;
        ProgressDialog progressDialog = new ProgressDialog(FirstActivity.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Busy loading rss feed...please wait...");
            progressDialog.show();
        }

        //Type3                Type1
        public String doInBackground(String... params) {
            try {

                //get the string url:
                String myUrl = params[0];

                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 );
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                InputStream inStream = conn.getInputStream();

// This code I used along with Volley libraty. It does not require asynctask.
//        private void parseJSON() {
//            String url = "https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Fworld%2Fus_and_canada%2Frss.xml";
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                JSONArray jsonArray = response.getJSONArray("items");
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject item = jsonArray.getJSONObject(i);
//
//                                    String artTitle = item.getString("title");
//                                    String artPubDate = item.getString("pubDate");
//                                    String description = item.getString("description");
//                                    String url = item.getString("link");
//
//                                    allArticles.add(new BbcArticles(artTitle, artPubDate, description, url));
//                                }
//
//                                mAdapter = new BbcNewsAdapter(MainActivity.this, allArticles);
//                                articleView.setAdapter(mAdapter);
////                                mAdapter.setOnItemClickListener(MainActivity.this);
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
//        }
//From part 3: slide 19
/*
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                //From part 3, slide 20

                boolean insideItem = false;
                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        String tagName = xpp.getName();
                        if (tagName.equals("item")) {
                            insideItem = true;
                            //If you get here, then you are pointing to a <Weather> start tag
                            title = xpp.getAttributeValue(null, "title");
                            Log.i("AsyncTask", "Found min message: " + title);
                            publishProgress(25);
                            Thread.sleep(500); //pause for 2000 millis

                            pubDate = xpp.getAttributeValue(null, "pubDate");
                            Log.i("AsyncTask", "Found min message: " + pubDate);
                            publishProgress(50);

                            Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                            description = xpp.getAttributeValue(null, "description");
                            Log.i("AsyncTask", "Found min message: " + description);
                            publishProgress(75);

                            Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                            webUrl = xpp.getAttributeValue(null, "link");
                            Log.i("AsyncTask", "Found min message: " + webUrl);
                            publishProgress(100);
                            Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                        }
                    }

                    xpp.next(); //advance to next XML event
                    if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eventType = xpp.next(); //move to next element
                }


            } catch (IOException ex) {
                Log.i("Crash!!", ex.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        //Type3
        public void onPostExecute(Exception s) {
            mAdapter = new BbcNewsAdapter(FirstActivity.this, allArticles);
            articleView.setAdapter(mAdapter);
            progressDialog.dismiss();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
*/
    public void buildRecyclerView() {
        articleView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        articleView.setLayoutManager(linearLayoutManager);
        articleView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allArticles = mDatabase.listArticles();
        if (allArticles.size() > 0) {
            articleView.setVisibility(View.VISIBLE);
            mAdapter = new BbcNewsAdapter(this, allArticles);
            articleView.setAdapter(mAdapter);
        } else {
            articleView.setVisibility(View.GONE);
            Toast.makeText(this, "There are no saved articles in Favorites list. Start adding now", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        // slide 15 material:
        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();

        sView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;

            }  });

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                message = "You clicked item 1";
                break;
            case R.id.search_item:
                message = getString(R.string.search_toast);
                break;
            case R.id.help_item:
                message = getString(R.string.help_toast);
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.help__menu_title))
                        .setMessage(getString(R.string.help_alert))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //code if yes
                            }
                        })
//                        .setNegativeButton("No", null)
                        .show();

                break;
            case R.id.mail:
                message = getString(R.string.mail_toast);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }
}