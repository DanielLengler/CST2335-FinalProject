package guardian;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asis.finalproject.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle dataFromActivity = getArguments();

        String title = dataFromActivity.getString(GuardianResults.TITLE);
        String url = dataFromActivity.getString(GuardianResults.URL);
        String sectionName = dataFromActivity.getString(GuardianResults.SECTION_NAME);

        TextView articleTitle = (TextView) result.findViewById(R.id.articleTitle);
        TextView articleUrl = (TextView) result.findViewById(R.id.articleUrl);
        TextView articleSection = (TextView) result.findViewById(R.id.articleSectionName);
        //Button addtoFavoritesButton = (Button) result.findViewById(R.id.addToFavorites);

        articleTitle.setText(title);
        articleUrl.setText(url);
        articleSection.setText(sectionName);

       /* addtoFavoritesButton.setOnClickListener((v) -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Add this article to favorites?");
            alertDialog.setMessage("Title: " + title + "\nUrl: " + url + "\nSection Name: " + sectionName);
            alertDialog.setPositiveButton("Yes", (click, arg) -> {favorites.add(selected); /*favoriteAdapter.notifyDataSetChanged(); */
          /*    Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();});
            alertDialog.setNegativeButton("No", (click, arg) -> {});
            alertDialog.create().show();*
        });*/

        articleUrl.setOnClickListener((v) -> {
            Intent viewWebPage = new Intent(Intent.ACTION_VIEW);
            viewWebPage.setData(Uri.parse(articleUrl.getText().toString()));
            startActivity(viewWebPage);
        });

        return result;

    }
}
