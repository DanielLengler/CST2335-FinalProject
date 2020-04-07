package com.asis.finalproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * BbcFavAdapter class
 * Connects data to RecyclerView and determines
 * ViewHolder for displaying the Data
 */

public class BbcFavAdapter extends RecyclerView.Adapter<BbcFavViewHolder>{
    private Context favContext;
    private ArrayList<BbcFavItem> favItemList;
    private BbcFavDB favDB;
    //    private ArrayList<BbcFavItem> mArrayList;
//

    public BbcFavAdapter( ArrayList<BbcFavItem> favItemList, Context favContext) {
        this.favContext = favContext;
//        this.mArrayList = favItemList;
        this.favItemList = favItemList;
    }
//    public void setFavList(List< BbcFavItem> favList) {
//        favItemList.clear();
//        favItemList.addAll(favList);
//        this.notifyItemRangeInserted(0, favItemList.size() - 1);
//    }



    @NonNull
    @Override
    public BbcFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_fav_item, parent, false);
        favDB = new BbcFavDB(favContext);
    return new BbcFavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BbcFavViewHolder holder, int position) {
        holder.favTitleView.setText(favItemList.get(position).getFav_title());
        holder.favPubDateView.setText(favItemList.get(position).getFav_pubDate());
        holder.favDescriptionView.setText(favItemList.get(position).getFav_description());
        holder.favLinkView.setText(favItemList.get(position).getFav_webUrl());
        holder.favBtn.setOnClickListener((view) -> {
             BbcFavDB favDB = new BbcFavDB(favContext);
            favDB.deleteArticle(favItemList.get(position).getId());
            ((Activity)favContext).finish();
            favContext.startActivity(((Activity)favContext).getIntent());
        });
    }


    @Override
    public int getItemCount () {
        if (favItemList == null)
            return 0;
        else
            return favItemList.size();
    }

//    public class BbcFavViewHolder extends RecyclerView.ViewHolder{
//        TextView favTitleView, favPubDateView, favDescriptionView, favLinkView;
//                Button favBtn;
//        public BbcFavViewHolder(@NonNull View itemView) {
//            super(itemView);
//            favTitleView = itemView.findViewById(R.id.artTitle);
//            favPubDateView = itemView.findViewById(R.id.artPubDate);
//            favDescriptionView = itemView.findViewById(R.id.artDescription);
//            favLinkView = itemView.findViewById(R.id.artLink);
//            favBtn = itemView.findViewById(R.id.favBtn);
//            //removes from favorite list after click
//            favBtn.setOnClickListener((view) -> {
//                int position = getAdapterPosition();
//                final BbcFavItem favItem = favItemList.get(position);
//                favDB.deleteArticle(favItem.getId());
//                removeItem(position);
//            });
//
//        }
//    }




//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String charString = charSequence.toString();
//                if (charString.isEmpty()) {
//                    favItemList = mArrayList;
//                }
//                else {
//                    ArrayList<BbcFavItem> filteredList = new ArrayList<>();
//                    for (BbcFavItem articles : mArrayList) {
//                        if (articles.getFav_title().toLowerCase().contains(charString)) {
//                            filteredList.add(articles);
//                        }
//                    }
//                    favItemList = filteredList;
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = favItemList;
//                return filterResults;
//            }
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                favItemList = (ArrayList<BbcFavItem>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

//    public class BbcFavViewHolder extends RecyclerView.ViewHolder{
//        TextView favTitleView, favPubDateView, favDescriptionView, favLinkView;
//        Button favBtn;
//
//        public BbcFavViewHolder(@NonNull View itemView) {
//            super(itemView);
//            favTitleView = itemView.findViewById(R.id.artTitle);
//            favPubDateView = itemView.findViewById(R.id.artPubDate);
//            favDescriptionView = itemView.findViewById(R.id.artDescription);
//            favLinkView = itemView.findViewById(R.id.artLink);
//            favBtn = itemView.findViewById(R.id.favBtn);
//            //removes from favorite list after click
//            favBtn.setOnClickListener((view) -> {
//                int position = getAdapterPosition();
//                final BbcFavItem favItem = favItemList.get(position);
//                favDB.deleteArticle(favItem.getId());
//                removeItem(position);
//            });
//        }
//    }
    private void removeItem ( int position){
        favItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favItemList.size());
    }

    /**
     *  Article search functionality step 3
     */

    public void filterList (ArrayList < BbcFavItem > filteredList) {
        favItemList = filteredList;
        notifyDataSetChanged();
    }
}