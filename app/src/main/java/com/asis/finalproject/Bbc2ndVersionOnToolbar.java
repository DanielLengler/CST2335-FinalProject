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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * Class that runs the second version of BBC news with direct access to BBc web
 */
public class Bbc2ndVersionOnToolbar extends AppCompatActivity {
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    ListView lvRss;
    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<String> descriptions;
    ArrayList<String> pubdates;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_2nd_version_on_toolbar);

        linearLayout = findViewById(R.id.linearLayout);
        /**
         * This gets the toolbar from the layout:
         */
        Toolbar tBar = findViewById(R.id.toolbar);
        //This loads the toolbar, which calls onCreateOptionsMenu below:
          setSupportActionBar(tBar);
        progressBar =  findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Inflate the menu items for use in the action bar
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

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

            }
        });

        return true;
    }

    /**
     * This method allows to select items on the toolbar
     * @param item
     * @return
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
            case R.id.search_item:
                message = getString(R.string.search_toast);
                break;
            case R.id.help_item:
                message = getString(R.string.help_toast);
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.help__menu_title))
                        .setMessage(getString(R.string.help_alert))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //code if yes
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

    /**
     *  This method is used to receive information from the stream
     * @param url
     * @return
     */
    public InputStream getInputStream(URL url)
    {
        try
        {
            /**
             *  openConnection() returns instance that represents a connection to the remote object referred to by the URL
             *  getInputStream() returns a stream that reads from the open connection
             */

            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    /**
     * Class ProgressInBackground
     * Helps to handle the Main UI Thread
     */
    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {
        ProgressDialog progressDialog = new ProgressDialog(Bbc2ndVersionOnToolbar.this);

        Exception exception = null;

        /**
         * This method is used to setup the task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Notifies the user on the task progress
             */
            progressDialog.setMessage(getString(R.string.progress_dialog_msg));
            progressDialog.show();
        }

        /**
         * This metod is responsible for parsing data from the XML files in
         * the Internet. It is the most long lasting method in this class
         * @param params
         * @return
         */
        @Override
        protected Exception doInBackground(Integer... params) {

            try {
                URL url = new URL("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml#");
                /**
                 * creates new instance of PullParserFactory that can be used to create XML pull parsers
                 */

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                /**
                 * Specifies whether the parser produced by this factory will provide support
                 * for XML namespaces
                 */

                factory.setNamespaceAware(false);
                /**
                 * creates a new instance of a XML pull parser using the currently configured
                 * factory features
                 */
                XmlPullParser xpp = factory.newPullParser();
                /**
                 * will get the XML from an input stream
                 */
                xpp.setInput(getInputStream(url), "UTF_8");
                /**
                 * This parses the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
                 * It should be taken into consideration that the rss feed name is also enclosed in a "<title>" tag.
                 * Every feed begins with these lines: "<channel><title>Feed_Name</title> etc."
                 * It will skip the "<title>" tag which is a child of "<channel>" tag,
                 * and take into consideration only the "<title>" tag which is a child of the "<item>" tag.
                 * It is achieved with assistance of a  boolean variable called "insideItem".
                  */

                boolean insideItem = false;
                /**
                 * Returns the type of current event: START_TAG, END_TAG, START_DOCUMENT, END_DOCUMENT etc.
                 */

                int eventType = xpp.getEventType();
                /**
                 * loops control variable
                 */
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    /**
                     * Checks whether it is at a START_TAG (opening tag)
                     */
                    if (eventType == XmlPullParser.START_TAG) {

                        /**
                         * Extracts the text between the title tags
                         * Checks if the tag is called "item"
                         */
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        }
                        /**
                         * Checks if the tag is called "title" and is located between the item tags
                         */
                        else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp.nextText());
                            }
                        }
                        /**
                         * Checks if the tag is called "description" and is located between the item tags
                         */
                        else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                descriptions.add(xpp.nextText());
                            }
                        }
                        /**
                         * Checks if the tag is called "link" and is located between the item tags
                         */
                        else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                /**
                                 * extracts the link
                                 */
                                links.add(xpp.nextText());
                            }
                        }
                        /**
                         * Checks if the tag is called "pubDate" and is located between the item tags
                         */
                        //
                        else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem) {
                                pubdates.add(xpp.nextText());
                            }
                        }
                    }
                    /**
                     * Checks whether it is reached the END_TAG and the END_TAG is called "item"
                     */
                    else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    /**
                     * moves to next element
                     */
                    eventType = xpp.next();
                }

                /**
                 * Catch block for potential exceptions
                 */
            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }

            return exception;
        }

        /**
         * This method displays the progress through the progress bar
         * @param values
         */
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        /**
         * Completes processing. The results of the doInBackground method are passed
         * to this method
         * @param s
         */
        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            adapter = new ArrayAdapter<String>(Bbc2ndVersionOnToolbar.this, android.R.layout.simple_list_item_1, titles);
            lvRss.setAdapter(adapter);
            progressDialog.dismiss();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
