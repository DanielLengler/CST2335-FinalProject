package com.asis.finalproject.guardian;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.R;
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

public class GuardianSearch extends AppCompatActivity {

    private ProgressBar progressBar;
    private ArrayList<Article> results = new ArrayList<>();
    private ArrayList<Article> favorites = new ArrayList<>();
    private ArrayList<JSONObject> jsonObjects = new ArrayList<>();
    private String modifiedApi = "https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=";
    private String searchFor;
    private MyAdapter adapter = new MyAdapter();
   // private ListView resultsList;
    //private String api = "https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=Tesla";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_search);
        Intent fromPrevious = getIntent();
        searchFor = fromPrevious.getStringExtra(GuardianSearchBar.SEARCH);
        Search search = new Search();
        progressBar = findViewById(R.id.guardianSearchProgress);
        progressBar.setVisibility(View.VISIBLE);
        search.execute(modifiedApi + searchFor);
        //search.execute(api);
        ListView resultsList = findViewById(R.id.listView);
        //Spinner favoritesList = findViewById(R.id.favorites);

        //MyAdapter adapter = new MyAdapter();
        //FavoriteAdapter favoriteAdapter = new FavoriteAdapter();
        resultsList.setAdapter(adapter);
        //favoritesList.setAdapter(favoriteAdapter);

        Button removeFromFavoritesButton = findViewById(R.id.removeFromFavoritesButton);


        resultsList.setOnItemClickListener((list, view, position, id) -> {
            Article selected = results.get(position);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Add this article to favorites?");
            alertDialog.setMessage("Title: " + selected.getTitle() + "\nUrl: " + selected.getUrl() + "\nSection Name: " + selected.getSectionName());
            alertDialog.setPositiveButton("Yes", (click, arg) -> {favorites.add(selected); /*favoriteAdapter.notifyDataSetChanged(); */
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();});
            alertDialog.setNegativeButton("No", (click, arg) -> {});
            alertDialog.create().show();
        });

        /*favoritesList.setOnItemClickListener((list, view, position, id) -> {
            Article selected = results.get(position);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Remove this article from favorites?");
            alertDialog.setMessage("Title: " + selected.getTitle() + "\nUrl: " + selected.getUrl() + "\nSection Name: " + selected.getSectionName());
            alertDialog.setPositiveButton("Yes", (click, arg) -> {favorites.remove(selected); favoriteAdapter.notifyDataSetChanged();
                Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_SHORT).setAction("Undo", (v) ->{favorites.add(selected);
                    favoriteAdapter.notifyDataSetChanged();});});
            alertDialog.setNegativeButton("No", (click, arg) -> {});
            alertDialog.create().show();
        });*/

        /*favoritesList.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Article selected = results.get(position);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GuardianSearch.class);
                        alertDialog.setTitle("Remove this article from favorites?");
                        alertDialog.setMessage("Title: " + selected.getTitle() + "\nUrl: " + selected.getUrl() + "\nSection Name: " + selected.getSectionName());
                        alertDialog.setPositiveButton("Yes", (click, arg) -> {favorites.remove(selected); favoriteAdapter.notifyDataSetChanged();
                            Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_SHORT).setAction("Undo", (v) ->{favorites.add(selected);
                                favoriteAdapter.notifyDataSetChanged();});});
                        alertDialog.setNegativeButton("No", (click, arg) -> {});
                        alertDialog.create().show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

        /*toFavoritesButton.setOnClickListener((v) -> {
            Intent nextActivity = new Intent(GuardianSearch.this, Favorite.class);
            //Bundle bundle = new Bundle();
            //bundle.putSerializable("Favorites", favorites);
            //nextActivity.putExtras(bundle);
           // nextActivity.putExtra("Favorites", favorites);

            for(int i=0; i<favorites.size(); i++){
                String fav = "Favorite " + i;
                nextActivity.putExtra(fav, favorites.get(i));
            }
            startActivity(nextActivity);
        });*/

        removeFromFavoritesButton.setOnClickListener((v) -> {
            favorites.clear();
            Snackbar.make(v, "Cleared favorites", Snackbar.LENGTH_SHORT).setAction("Undo", click ->
                    Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()).show();
        });




    }


    class MyAdapter extends BaseAdapter {

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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Article article = (Article) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.activity_guardian_search_populate, parent, false);
            TextView textView = newView.findViewById(R.id.results);
            textView.setText(article.getTitle());
            return newView;
        }
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
            View newView = inflater.inflate(R.layout.activity_guardian_search_populate, parent, false);
            TextView textView = newView.findViewById(R.id.results);
            textView.setText(favorites.getTitle());
            return newView;
        }
    }

    class Search extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(modifiedApi + searchFor);
                //URL url = new URL(api);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                publishProgress(50);

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jObject = new JSONObject(result);
                JSONObject responseObject = jObject.getJSONObject("response");
                JSONArray jArray = responseObject.getJSONArray("results");

                for(int i=0; i<jArray.length(); i++){
                    try{
                        JSONObject anObject = jArray.getJSONObject(i);
                        Article found = new Article(anObject.getString("webTitle"), anObject.getString("webUrl"),
                                anObject.getString("sectionName"));
                        results.add(found);
                        /*anObject.getString("webTitle");
                        anObject.getString("webUrl");
                        anObject.getString("sectionName");*/
                    }catch(JSONException e){}
                }

                publishProgress(75);

            } catch (MalformedURLException e) {
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
            /*resultsList = findViewById(R.id.listView);
            MyAdapter resultsAdapter = new MyAdapter();
            resultsList.setAdapter(resultsAdapter);*/
            adapter.notifyDataSetChanged();

        }
    }
}
