package guardian;

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
    private String modifiedUrl = "https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=";
    private String searchFor;
    private String pageSize = "&page-size=50";
    private ProgressBar progressBar;

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

    private long insertIntoDataBase(Article article){
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(MyOpener.COL_TITLE, article.getTitle());
        newRowValues.put(MyOpener.COL_URL, article.getUrl());
        newRowValues.put(MyOpener.COL_SECTION_NAME, article.getSectionName());
        return dataBase.insert(MyOpener.TABLE_NAME, null, newRowValues);
    }

    private void deleteFromDataBase(Article article){
        dataBase.delete(MyOpener.TABLE_NAME, MyOpener.COL_TITLE + " = ?", new String[]{article.getTitle()});
    }

    private boolean checkIfExistsInDataBase(Article article){
        Cursor results = dataBase.rawQuery("SELECT * FROM " + MyOpener.TABLE_NAME + " WHERE " + MyOpener.COL_TITLE + " = ? AND " + MyOpener.COL_URL + "= ? AND "
            + MyOpener.COL_SECTION_NAME + "= ?", new String[]{article.getTitle(), article.getUrl(), article.getSectionName()});

        if(results.getCount() > 0){ return true; }
        else{ return false; }
    }

    private void displayHelp(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.tutorialTitle));
        alertDialog.setMessage(getResources().getString(R.string.tutorialFull));
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), (click, arg) -> {});
        alertDialog.create().show();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guardian_toolbar_menu, menu);
        return true;
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Article getItem(int position) {
            return results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

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

    class Search extends AsyncTask<String, Integer, String> {

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
                        results.add(new Article(articleTitle, articleUrl, articleSection, 0));
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

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }


}
