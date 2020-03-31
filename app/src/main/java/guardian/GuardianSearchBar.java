package guardian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.asis.finalproject.R;
import com.google.android.material.navigation.NavigationView;

public class GuardianSearchBar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SEARCH = "Search";
    public static final String FILE_NAME = "FileName";
    public static final String KEY = "Saved Search";
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_searchbar);

        setupActionBarAndDrawer();
        EditText search = findViewById(R.id.guardianSearchBar);
        Button toResults = findViewById(R.id.guardianSearchCriteriaButton);

        prefs = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        search.setText(prefs.getString(KEY, ""));

        toResults.setOnClickListener((v)->{
            saveSharedPrefs(search.getText().toString());
            Intent nextActivity = new Intent(GuardianSearchBar.this, GuardianResults.class);
            nextActivity.putExtra(SEARCH, search.getText().toString());
            startActivity(nextActivity);
        });
    }

    private void saveSharedPrefs(String toSave){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY, toSave);
        editor.commit();

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
                startActivity(new Intent(GuardianSearchBar.this, Favorite.class));
                break;
            case R.id.searchToolbarItem:
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
                startActivity(new Intent(GuardianSearchBar.this, Favorite.class));
                break;
            case R.id.searchToolbarItem:
                break;
            case R.id.helpToolbarItem:
                displayHelp();
                break;
        }
        return true;
    }
}
