package com.asis.finalproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


/**
 * BbcFavAdapter class
 * Connects data to RecyclerView and determines
 * ViewHolder for displaying the Data
 */

public class BbcFavAdapter extends RecyclerView.Adapter<BbcFavViewHolder>{
    private Context favContext;
    /**
     * ArrayList of BBC favorite articles located in BbcFavDB database
     */
    private ArrayList<BbcFavItem> favItemList;
//    private ArrayList<BbcFavItem> mArrayList;
    /**
     * database of favorite Articles
     */
    private BbcFavDB favDB;


    /**
     * BbcFavAdapter Constructor that includes:
     * @param favItemList ArrayList of favorite articles
     * @param favContext content of the favorite articles
     */
    public BbcFavAdapter( ArrayList<BbcFavItem> favItemList, Context favContext) {
        this.favContext = favContext;
        this.favItemList = favItemList;
    }

    /**
     *
     * @param parent is a grouping parameter
     * @param viewType initializes the layout
     * @return a new ViewHolder object
     */
    @NonNull
    @Override
    public BbcFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_fav_item, parent, false);
        favDB = new BbcFavDB(favContext);
    return new BbcFavViewHolder(view);
    }


    /**
     * This binds content (data) with views of RecyclerView
     * @param holder holds position and content of the item together
     * @param position identifies position of each element
     */
    @Override
    public void onBindViewHolder(BbcFavViewHolder holder, int position) {
        holder.favTitleView.setText(favItemList.get(position).getFav_title());
        holder.favPubDateView.setText(favItemList.get(position).getFav_pubDate());
        holder.favDescriptionView.setText(favItemList.get(position).getFav_description());
        holder.favLinkView.setText(favItemList.get(position).getFav_webUrl());
        holder.deleteBtn.setOnClickListener((view) -> {
            favDB.deleteArticle(favItemList.get(position).getId());
            removeItem(position);
        });
    }

    /**
     * Returns the total number of items held by adapter
     * @return the size of items in the arrayList
     */
    @Override
    public int getItemCount () {
        if (favItemList == null)
            return 0;
        else
            return favItemList.size();
    }



    /**
     * This method removes items from favorite list
     * @param position indicates the position of removing item
     */
    private void removeItem ( int position){
        favItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favItemList.size());
    }

    /**
     * This method deletes items from favorite list
     * @param position indicates the position of removing item
     */
    public void deleteArticle(int position){
        favDB.deleteArticle(position);
        notifyDataSetChanged();
    }


    /**
     *  Article search functionality step 3
     */

    public void filterList (ArrayList < BbcFavItem > filteredList) {
        favItemList = filteredList;
        notifyDataSetChanged();
    }
}