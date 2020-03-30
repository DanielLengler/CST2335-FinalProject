package com.asis.finalproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BbcNewsFavoriteAdapter extends RecyclerView.Adapter<BbcNewsFavoriteViewHolder> implements Filterable{
    private Context context;
    private ArrayList<BbcArticles> listArticles;
    private ArrayList<BbcArticles> mArrayList;
    private SqliteDatabase mDatabase;

    BbcNewsFavoriteAdapter(Context context, ArrayList<BbcArticles> listArticles) {
        this.context = context;
        this.listArticles = listArticles;
        this.mArrayList = listArticles;
        mDatabase = new SqliteDatabase(context);
    }
    @Override
    public BbcNewsFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_article_row_delete, parent, false);
        return new BbcNewsFavoriteViewHolder(view);
    }
    @Override
    public void onBindViewHolder(BbcNewsFavoriteViewHolder holder, int position) {

        final BbcArticles articles = listArticles.get(position);
        holder.favTitle.setText(articles.getTitle());
        holder.favPubDate.setText(articles.getPubDate());
        holder.favDescription.setText(articles.getDescription());
        holder.favUrlLink.setText(articles.getWebUrl());
        holder.deleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.deleteArticle(articles.getId());
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listArticles = mArrayList;
                }
                else {
                    ArrayList<BbcArticles> filteredList = new ArrayList<>();
                    for (BbcArticles articles : mArrayList) {
                        if (articles.getTitle().toLowerCase().contains(charString)) {
                            filteredList.add(articles);
                        }
                    }
                    listArticles = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listArticles;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listArticles = (ArrayList<BbcArticles>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return listArticles.size();
    }

    //search functionality step 3
    public void filterList(ArrayList<BbcArticles> filteredList){
        listArticles = filteredList;
        notifyDataSetChanged();
    }

}
