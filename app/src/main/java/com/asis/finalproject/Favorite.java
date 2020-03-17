package com.asis.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Favorite extends AppCompatActivity {

    ArrayList<Article> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //Bundle dataFromActivity = getIntent().getExtras();
        //favorites = dataFromActivity.getSerializableExtra("Favorites");
        favorites = (ArrayList<Article>) getIntent().getSerializableExtra("Favorites");
        ListView favoriteList = findViewById(R.id.favoriteActivityListView);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter();
        favoriteList.setAdapter(favoriteAdapter);
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


}


