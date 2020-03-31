package guardian;

/**
 * @author Naimul Rahman
 * @class GuardianResults
 * @version 3
 * This class is used with the activity_guardian_results layout. It displays the results for what the
 * user searched for from @class GuardianSearchBar. Articles found are displayed in a ListView in which
 * the user can click on. If the user is using a tablet, the information about the article will be displayed
 * on the right and they can click the url for the article to view the webpage. If the user is on a phone,
 * it will take them to the appropriate fragment to display that same information. Users can add any
 * article they wish to a favorites list. If an article is in their favorites list, the star beside the
 * article will be filled yellow. If not, then it will be an empty star.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asis.finalproject.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GuardianResults extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Article> results = new ArrayList<>();
    private ArrayList<Article> favorites = new ArrayList<>();
    private Adapter adapter = new Adapter();
    private SQLiteDatabase dataBase;
    private MyOpener dbOpener = new MyOpener(this);
    private DetailsFragment detailsFragment;
    public static final String TITLE = "Title";
    public static final String URL = "Url";
    public static final String SECTION_NAME = "Section Name";
    // private String originalUrl = "https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=Tesla";
    /*
     * The api provided by the professor. It's been modified to remove Tesla from the search. Will be concatenated with whatever the user wanted to search
     * from @class GuardianSearchBar to search for that instead.
     */
    private String modifiedUrl = "https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=";
    private String searchFor; // Will be set to what the user wanted to search for from @class GuardianSearchBar in @onCreate
    private String pageSize = "&page-size=50"; // used to display more results in the list. Found with the guardian's api documentation: https://open-platform.theguardian.com/documentation/search
    private ProgressBar progressBar;

    /**
     * This method calls other methods to do what needs to be done. It loads the favorites list from the database,
     * displays the results list, and allows you to add to your favorites list (or remove if you accidentally added something
     * you did not). It basically sets an onClickListener and onItemLongClickListener to the ListView, sets up the toolbar and
     * navigation drawer, and calls appropriate methods for different actions.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_results);

        boolean isTablet = findViewById(R.id.frameLayout) != null;
        setupActionBarAndDrawer();
        loadFromDatabase();

        Intent fromActivity = getIntent();
        searchFor = fromActivity.getStringExtra(GuardianSearchBar.SEARCH);
        progressBar = findViewById(R.id.guardianSearchProgress);
        progressBar.setVisibility(View.VISIBLE);
        ListView resultsList =findViewById(R.id.listView);
        resultsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        new Search().execute(modifiedUrl + searchFor + pageSize);

        resultsList.setOnItemClickListener((list, view, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(TITLE, results.get(position).getTitle());
            dataToPass.putString(URL, results.get(position).getUrl());
            dataToPass.putString(SECTION_NAME, results.get(position).getSectionName());
            if(isTablet){
                detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(dataToPass);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, detailsFragment).commit();
            }

            else{
                Intent nextActivity = new Intent(GuardianResults.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }
        });

        resultsList.setOnItemLongClickListener((list, view, position, id) -> {
            Article selected = results.get(position);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.addToFavoritesQuestion));
            alertDialog.setMessage(getResources().getString(R.string.title) + selected.getTitle() + "\n" + getResources().getString(R.string.url)
                    + selected.getUrl() + "\n" + getResources().getString(R.string.sectionName) + selected.getSectionName());
            alertDialog.setPositiveButton(getResources().getString(R.string.yes), (click, arg) -> {
                if(!checkIfExistsInDataBase(selected)) {
                    insertIntoDataBase(selected);
                    favorites.add(selected);
                    adapter.notifyDataSetChanged();
                    Snackbar.make(view, getResources().getString(R.string.addedToFavorites), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.undo), (clickSnackBar) -> {
                        deleteFromDataBase(selected);
                        favorites.remove(selected);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, getResources().getString(R.string.removedFromFavorites), Toast.LENGTH_SHORT).show();
                    }).show();
                }

                else{
                    Toast.makeText(this, getResources().getString(R.string.alreadyInFavorites), Toast.LENGTH_SHORT).show();
                }

            });
            alertDialog.setNegativeButton(getResources().getString(R.string.no), (click, arg) -> {});
            alertDialog.create().show();
            return true;});
    }

    /**
     * This methods loads all the articles stored in favorites and adds them to the favorites ArrayList.
     */
    private void loadFromDatabase(){
        dataBase = dbOpener.getWritableDatabase();
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_TITLE, MyOpener.COL_URL, MyOpener.COL_SECTION_NAME};
        Cursor resultsQuery = dataBase.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int idColIndex = resultsQuery.getColumnIndex(MyOpener.COL_ID);
        int titleColIndex = resultsQuery.getColumnIndex(MyOpener.COL_TITLE);
        int urlColIndex = resultsQuery.getColumnIndex(MyOpener.COL_URL);
        int sectionColIndex = resultsQuery.getColumnIndex(MyOpener.COL_SECTION_NAME);

        while(resultsQuery.moveToNext()){
            long id = resultsQuery.getLong(idColIndex);
            String title = resultsQuery.getString(titleColIndex);
            String url = resultsQuery.getString(urlColIndex);
            String section = resultsQuery.getString(sectionColIndex);
            favorites.add(new Article(title, url, section, id));
        }
    }

    /**
     * This method inserts an article to the user's favorites list in the database.
     * @param article The article to be stored to the database.
     * @return The ID of the article, a long incremented automatically by the database. This ID is provided by the database.
     */
    private long insertIntoDataBase(Article article){
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(MyOpener.COL_TITLE, article.getTitle());
        newRowValues.put(MyOpener.COL_URL, article.getUrl());
        newRowValues.put(MyOpener.COL_SECTION_NAME, article.getSectionName());
        return dataBase.insert(MyOpener.TABLE_NAME, null, newRowValues);
    }

    /**
     * This method deletes an article from the favorites database.
     * @param article The article to be deleted.
     */
    private void deleteFromDataBase(Article article){
        dataBase.delete(MyOpener.TABLE_NAME, MyOpener.COL_TITLE + " = ?", new String[]{article.getTitle()});
    }

    /**
     * This method checks if the article already exists in the database or not.
     * @param article The article whose existance is to be checked in the database.
     * @return true if the article is already in the database, false if not.
     */
    private boolean checkIfExistsInDataBase(Article article){
        Cursor results = dataBase.rawQuery("SELECT * FROM " + MyOpener.TABLE_NAME + " WHERE " + MyOpener.COL_TITLE + " = ? AND " + MyOpener.COL_URL + "= ? AND "
            + MyOpener.COL_SECTION_NAME + "= ?", new String[]{article.getTitle(), article.getUrl(), article.getSectionName()});

        if(results.getCount() > 0){ return true; }
        else{ return false; }
    }

    /**
     * This method displays an AlertDialog with a set of instructions on how to use this application
     */
    private void displayHelp(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.tutorialTitle));
        alertDialog.setMessage(getResources().getString(R.string.tutorialFull));
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), (click, arg) -> {});
        alertDialog.create().show();
    }

    /**
     * This method sets up the toolbar, navigation drawer, and actionbar for this layout.
     */
    private void setupActionBarAndDrawer() {
        //For ToolBar:
        Toolbar toolbar = findViewById(R.id.guardianToolbar);
        setSupportActionBar(toolbar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.guardianDrawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.guardian_Nav_View);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * This method is used to inflate the toolbar menu.
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guardian_toolbar_menu, menu);
        return true;
    }

    /**
     * This method is used whenever the user taps or clicks on one of the toolbar items. When favorites
     * is clicked (the star), it will take the user to the @class Favorite activity. When the user clicks the
     * help icon (the icon with an "i"), it will display the tutorial by called @displayHelp. When the
     * user clicks the search icon (the magnifying glass), it will take the user to @class GuardianSearchBar to enter
     * something else to search for.
     * @param menuItem
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.favoritesToolbarItem:
                startActivity(new Intent(GuardianResults.this, Favorite.class));
                break;
            case R.id.searchToolbarItem:
                startActivity(new Intent(GuardianResults.this, GuardianSearchBar.class));
                break;
            case R.id.helpToolbarItem:
                displayHelp();
                break;
        }
        return true;
    }

    /**
     * This method is used whenever the user taps or clicks on one of the navigation drawer items. When favorites
     * is clicked (the star), it will take the user to the @class Favorite activity. When the user clicks the
     * help icon (the icon with an "i"), it will display the tutorial by called @displayHelp. When the
     * user clicks the search icon (the magnifying glass), it will take the user to @class GuardianSearchBar to enter
     * something else to search for.
     * @param item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favoritesToolbarItem:
                startActivity(new Intent(GuardianResults.this, Favorite.class));
                break;
            case R.id.searchToolbarItem:
                startActivity(new Intent(GuardianResults.this, GuardianSearchBar.class));
                break;
            case R.id.helpToolbarItem:
                displayHelp();
                break;
        }
        return true;
    }

    /**
     * This method is automatically called when the user goes back to this activity if they pressed the
     * back button on their device or another method. It updates the GUI in case there's any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**
     * This is an inner class that acts as an adapter for the ListView to populate it with results.
     */
    class Adapter extends BaseAdapter {

        /**
         * Gets the number of elements in the results ArrayList.
         * @return The number of results in the ArrayList.
         */
        @Override
        public int getCount() {
            return results.size();
        }

        /**
         * Gets the article stored in its designated position in the results ArrayList.
         * @param position the index where the article is stored in the ArrayList.
         * @return The article.
         */
        @Override
        public Article getItem(int position) {
            return results.get(position);
        }

        /**
         * Gets the ID of the article in the ArrayList.
         * @param position the index where the article is in the ArrayList.
         * @return the article's ID.
         */
        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        /**
         * Populates the results ArrayList with the results found from the search. If the article
         * exists in the favorites database, it will fill the star yellow, otherwise it will
         * keep it empty.
         * @param position The index where the article exists in the ArrayList.
         * @param convertView Allows the ListView to save memory. We don't use this.
         * @param parent The parent layout.
         * @return The layout you want to inflate the ListView with.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Article results = (Article) getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            if(!checkIfExistsInDataBase(results)) {
                View newView = inflater.inflate(R.layout.activity_guardian_search_populate, parent, false);
                TextView textView = newView.findViewById(R.id.results);
                textView.setText(results.getTitle());
                return newView;
            }

            else{
                View newView = inflater.inflate(R.layout.activity_guardian_search_populate_favorited, parent, false);
                TextView textView = newView.findViewById(R.id.results);
                textView.setText(results.getTitle());
                return newView;
            }
        }
    }

    /**
     * This class is used to retrieve the search results
     */
    class Search extends AsyncTask<String, Integer, String> {

        /**
         * This method makes a connection with the url api provided by the professor, and
         * concatenates it with what the user wanted to search for and sets the page-size to 50
         * to allow more results to be displayed in the results ListView.
         * @param strings
         * @return null
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                java.net.URL url = new URL(modifiedUrl + searchFor + pageSize);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                publishProgress(50);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                JSONObject jobject = new JSONObject(result);
                JSONObject responseObject = jobject.getJSONObject("response");
                JSONArray jArray = responseObject.getJSONArray("results");
                publishProgress(75);

                for (int i=0; i < jArray.length(); i++)
                    try {
                        JSONObject retrievedResult = jArray.getJSONObject(i);
                        String articleTitle = retrievedResult.getString("webTitle");
                        String articleUrl = retrievedResult.getString("webUrl");
                        String articleSection = retrievedResult.getString("sectionName");
                        results.add(new Article(articleTitle, articleUrl, articleSection));
                        } catch (JSONException e) { }


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * This method updates the progress bar to allow seeing it fill up as necessary
         * @param values The values when publishProgress() is called in doInBackground()
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(values[0]);
        }

        /**
         * This method updates the GUI when everything's done.
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }


}
