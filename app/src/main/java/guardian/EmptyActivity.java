package guardian;

/**
 * @author Naimul Rahman
 * @class EmptyActivity
 * @version 3
 * This class acts as a bridge between @GuardianResults and @Favorite to @DetailFragment. Used
 * to pass information from one of those activities to the fragment if the user is on a phone.
 * Immediately takes them to the fragment.
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.asis.finalproject.R;

public class EmptyActivity extends AppCompatActivity {

    /**
     * All this method does is get the values passed from eiher @GuardianResults or @Favorite
     * and pass them as a Bundle to @class DetailsFragment before taking the user there.
     * @param savedInstanceState
     */
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
