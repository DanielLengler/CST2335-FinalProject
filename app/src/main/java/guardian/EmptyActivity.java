package guardian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.asis.finalproject.R;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Bundle dataToPass = getIntent().getExtras();
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(dataToPass);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, detailsFragment).commit();
    }
}
