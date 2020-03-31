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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asis.finalproject.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import guardian.Article;

public class Favorite extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Article> favorites = new ArrayList<>();
    private SQLiteDatabase dataBase;
    private MyOpener dbOpener = new MyOpener(this);
    private DetailsFragment detailsFragment;
    private static final String TITLE = "Title";
    private static final String URL = "Url";
    private static final String SECTION_NAME = "Section Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        boolean isTablet = findViewById(R.id.favoriteFrameLayout) != null;
        setupActionBarAndDrawer();
        loadFromDatabase();
        ListView favoriteList = findViewById(R.id.favoriteActivityListView);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter();
        favoriteList.setAdapter(favoriteAdapter);

        favoriteList.setOnItemClickListener((list, view, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(TITLE, favorites.get(position).getTitle());
            dataToPass.putString(URL, favorites.get(position).getUrl());
            dataToPass.putString(SECTION_NAME, favorites.get(position).getSectionName());
            if(isTablet){
                detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(dataToPass);
                getSupportFragmentManager().beginTransaction().replace(R.id.favoriteFrameLayout, detailsFragment).commit();
            }

            else{
                Intent nextActivity = new Intent(Favorite.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }
        });

        favoriteList.setOnItemLongClickListener((list, view, position, id) -> {
            Article selected = favorites.get(position);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.deleteFromFavoritesQuestion));
            alertDialog.setMessage(getResources().getString(R.string.title) + selected.getTitle() + "\n" + getResources().getString(R.string.url) +
                    selected.getUrl() + "\n" + getResources().getString(R.string.sectionName) + selected.getSectionName());
            alertDialog.setPositiveButton(getResources().getString(R.string.yes), (click, arg) -> {

                    deleteFromDataBase(selected);
                    favorites.remove(selected);
                    favoriteAdapter.notifyDataSetChanged();

                    Toast.makeText(this, getResources().getString(R.string.deletedFromFavorites), Toast.LENGTH_SHORT).show();


            });
            alertDialog.setNegativeButton(getResources().getString(R.string.no), (click, arg) -> {});
            alertDialog.create().show();
            return true;
        });
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
                break;
            case R.id.searchToolbarItem:
                startActivity(new Intent(Favorite.this, GuardianSearchBar.class));
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
                break;
            case R.id.searchToolbarItem:
                startActivity(new Intent(Favorite.this, GuardianSearchBar.class));
                break;
            case R.id.helpToolbarItem:
                displayHelp();
                break;
        }
        return true;
    }

    class FavoriteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favorites.size();
        }

        @Override
        public Article getItem(int position) {
            return favorites.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Article favorites = (Article) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.activity_guardian_search_populate_favorited, parent, false);
            TextView textView = newView.findViewById(R.id.results);
            textView.setText(favorites.getTitle());
            return newView;
        }
    }


}


