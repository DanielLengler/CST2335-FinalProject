package com.asis.finalproject.bbc;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asis.finalproject.R;

import java.util.ArrayList;

/**
 * Class BbcFavViewHolder describes an item view and its location within the RecyclerView
 */
public class BbcFavViewHolder extends RecyclerView.ViewHolder {

    private ArrayList<BbcFavItem> favItemList;
    private BbcFavDB favDB;
    TextView favTitleView, favPubDateView, favDescriptionView, favLinkView;
    Button deleteBtn;

    /**
     * Constructor of BbcFavViewHolder class
     * @param itemView gives references to the view of favorite articles in rows
     */
    public BbcFavViewHolder(@NonNull View itemView) {
            super(itemView);
            favTitleView = itemView.findViewById(R.id.artTitle);
            favPubDateView = itemView.findViewById(R.id.artPubDate);
            favDescriptionView = itemView.findViewById(R.id.artDescription);
            favLinkView = itemView.findViewById(R.id.artLink);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            deleteBtn.setOnClickListener((view) -> {
                int position = getAdapterPosition();
                final BbcFavItem favItem = favItemList.get(position);
                favDB.deleteArticle(favItem.getId());
                removeItem(position);
            });

    }

    /**
     * This method removes items from favorite list
     * @param position the position of the removing item in the list
     */
    private void removeItem(int position){
        favItemList.remove(position);

    }

}