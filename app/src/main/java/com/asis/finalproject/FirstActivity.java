package com.asis.finalproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class FirstActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private Button favorButton;
    //**************************
    ListView lvRss;
    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<String> descriptions;
    ArrayList<String> pubdates;
    ArrayAdapter<String> adapter;
    //**************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        linearLayout = findViewById(R.id.linearLayout);
        favorButton = findViewById(R.id.myFavorites_btn);

        favorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showSnackbar();
            }
        });

        //This gets the toolbar from the layout:
        Toolbar tBar = findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        //  setSupportActionBar(tBar);

        progressBar =  findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
//**********************************************
        lvRss = findViewById(R.id.lvRss);

        titles = new ArrayList<>();
        links = new ArrayList<>();
        descriptions = new ArrayList<>();
        pubdates = new ArrayList<>();


        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri uri = Uri.parse(links.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        new ProcessInBackground().execute();
//***********************************************
    }
    public void showSnackbar(){
        Snackbar snackbar = Snackbar.make(linearLayout,getText(R.string.snackbar_message),Snackbar.LENGTH_INDEFINITE)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar1  = Snackbar.make(linearLayout, getText(R.string.undo_message), Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                });
        snackbar.show();

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
                adapter.getFilter().filter(newText);
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

    //*******************************************************************
    public InputStream getInputStream(URL url)
    {
        try
        {
            //openConnection() returns instance that represents a connection to the remote object referred to by the URL
            //getInputStream() returns a stream that reads from the open connection
            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {
        ProgressDialog progressDialog = new ProgressDialog(FirstActivity.this);

        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Busy loading rss feed...please wait...");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... params) {

            try {
                URL url = new URL("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml#");

                //creates new instance of PullParserFactory that can be used to create XML pull parsers
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                //Specifies whether the parser produced by this factory will provide support
                //for XML namespaces
                factory.setNamespaceAware(false);

                //creates a new instance of a XML pull parser using the currently configured
                //factory features
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");

                /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
                 * We should take into consideration that the rss feed name is also enclosed in a "<title>" tag.
                 * Every feed begins with these lines: "<channel><title>Feed_Name</title> etc."
                 * We should skip the "<title>" tag which is a child of "<channel>" tag,
                 * and take into consideration only the "<title>" tag which is a child of the "<item>" tag
                 *
                 * In order to achieve this, we will make use of a boolean variable called "insideItem".
                 */

                boolean insideItem = false;

                // Returns the type of current event: START_TAG, END_TAG, START_DOCUMENT, END_DOCUMENT etc..
                int eventType = xpp.getEventType(); //loop control variable

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    //if we are at a START_TAG (opening tag)
                    if (eventType == XmlPullParser.START_TAG) {
                        //if the tag is called "item"
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        }
                        //if the tag is called "title"
                        else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                // extract the text between <title> and </title>
                                titles.add(xpp.nextText());
                            }
                        }
                        //if the tag is called "description"
                        else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                // extract the text between <title> and </title>
                                descriptions.add(xpp.nextText());
                            }
                        }
                        //if the tag is called "link"
                        else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                // extract the text between <link> and </link>
                                links.add(xpp.nextText());
                            }
                        }
                        //if the tag is called "pubDate"
                        else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem) {
                                // extract the text between <title> and </title>
                                pubdates.add(xpp.nextText());
                            }
                        }
                    }
                    //if we are at an END_TAG and the END_TAG is called "item"
                    else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }

                    eventType = xpp.next(); //move to next element
                }


            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }

            return exception;
        }


        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            adapter = new ArrayAdapter<String>(FirstActivity.this, android.R.layout.simple_list_item_1, titles);
            lvRss.setAdapter(adapter);
            progressDialog.dismiss();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    //*****************************************************************

}