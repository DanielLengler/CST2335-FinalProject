package guardian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.R;

public class GuardianSearchBar extends AppCompatActivity {

    public static final String SEARCH = "Search";
    public static final String FILE_NAME = "FileName";
    public static final String KEY = "Saved Search";
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_searchbar);

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
}
