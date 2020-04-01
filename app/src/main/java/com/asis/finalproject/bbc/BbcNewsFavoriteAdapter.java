package com.asis.finalproject.bbc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asis.finalproject.R;

import java.util.ArrayList;

/**
 * BbcNewsFavoriteAdapter class
 * Connects data to RecyclerView and determines
 * ViewHolder for displaying the Data
 */
public class BbcNewsFavoriteAdapter extends RecyclerView.Adapter <BbcNewsFavoriteAdapter.BbcNewsFavoriteViewHolder>{
    private Context favContext;
    private ArrayList<BbcArticles> favArticleList;

    /**
     * Constructor of the BbcNewsFavoriteAdapter
     * @param favContext
     * @param favArticleList
     */
    public BbcNewsFavoriteAdapter(Context favContext, ArrayList<BbcArticles> favArticleList) {
        this.favContext = favContext;
        this.favArticleList = favArticleList;
    }
    /**
     * BbcNewsFavoriteAdapter constuctor
     * @param articleList
     */
    public BbcNewsFavoriteAdapter(ArrayList<BbcArticles> articleList){
        favArticleList = articleList;
    }

    /**
     * This method is called right when the adapter is created and is used to
     * initialize the ViewHolder(s).
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public BbcNewsFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_article_row_delete, parent,false);
        BbcNewsFavoriteAdapter.BbcNewsFavoriteViewHolder evh = new BbcNewsFavoriteAdapter.BbcNewsFavoriteViewHolder(v);
        return evh;
    }

    /**
     * This method is called for each ViewHolder to bind it to the adapter.
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(@NonNull BbcNewsFavoriteViewHolder holder, int position) {
        BbcArticles currentItem = favArticleList.get(position);
        holder.favTitle.setText(currentItem.getTitle());
        holder.favPubDate.setText(currentItem.getPubDate());
        holder.favDescription.setText(currentItem.getDescription());
        holder.favWebLink.setText(currentItem.getWebUrl());
    }

    /**
     * Returns the size of the collection that contains the items we want to display
     * @return
     */

    @Override
    public int getItemCount() {
        if (favArticleList == null)
            return 0;
        else
            return favArticleList.size();
    }

    /**
     * BbcNewsFavoriteViewHolder  class
     * Holds the article’s views
     */

    public static class BbcNewsFavoriteViewHolder extends RecyclerView.ViewHolder{
        public TextView favTitle;
        public TextView favPubDate;
        public TextView favDescription;
        public TextView favWebLink;

        public BbcNewsFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favTitle = itemView.findViewById(R.id.artTitle);
            favPubDate=itemView.findViewById(R.id.artPubDate);
            favDescription = itemView.findViewById(R.id.artDescription);
            favWebLink = itemView.findViewById(R.id.artLink);

        }
    }

    /**
     *  Article search functionality step 3
     */

    public void filterList(ArrayList<BbcArticles> filteredList){
        favArticleList = filteredList;
        notifyDataSetChanged();
    }
}
