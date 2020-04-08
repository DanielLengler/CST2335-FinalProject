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
    private ArrayList<BbcFavItem> favItemList;
    private ArrayList<BbcFavItem> mArrayList;
    private BbcFavDB favDB;


    /**
     *
     * @param favItemList
     * @param favContext
     */
    public BbcFavAdapter( ArrayList<BbcFavItem> favItemList, Context favContext) {
        this.favContext = favContext;
        this.favItemList = favItemList;
    }

//    public interface OnItemClickListener{
//        void onDeleteClick(int position);
//    }
//    public void setOnItemClickListener(OnItemClickListener listener){
//        mListener = listener;
//    }

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
            ((Activity)favContext).finish();
            favContext.startActivity(((Activity)favContext).getIntent());
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



//    public class BbcFavViewHolder extends RecyclerView.ViewHolder{
//        TextView favTitleView, favPubDateView, favDescriptionView, favLinkView;
//        Button deleteBtn;
//
//        public BbcFavViewHolder(@NonNull View itemView) {
//            super(itemView);
//            favTitleView = itemView.findViewById(R.id.artTitle);
//            favPubDateView = itemView.findViewById(R.id.artPubDate);
//            favDescriptionView = itemView.findViewById(R.id.artDescription);
//            favLinkView = itemView.findViewById(R.id.artLink);
//            deleteBtn = itemView.findViewById(R.id.favBtn);
//            //removes from favorite list after click
//            deleteBtn.setOnClickListener((view) -> {
//                int position = getAdapterPosition();
//                final BbcFavItem favItem = favItemList.get(position);
//                favDB.deleteArticle(favItem.getId());
//                removeItem(position);
//            });
//        }
//    }

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