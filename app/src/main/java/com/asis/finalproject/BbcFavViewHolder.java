package com.asis.finalproject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 *
 */
public class BbcFavViewHolder extends RecyclerView.ViewHolder {

    private ArrayList<BbcFavItem> favItemList;
    private BbcFavDB favDB;
    TextView favTitleView, favPubDateView, favDescriptionView, favLinkView;
    Button deleteBtn;

    /**
     *
      * @param itemView we get references to our favorite article views in rows
     */
    public BbcFavViewHolder(@NonNull View itemView) {
            super(itemView);
            favTitleView = itemView.findViewById(R.id.artTitle);
            favPubDateView = itemView.findViewById(R.id.artPubDate);
            favDescriptionView = itemView.findViewById(R.id.artDescription);
            favLinkView = itemView.findViewById(R.id.artLink);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            //removes from favorite list after click
            deleteBtn.setOnClickListener((view) -> {
                int position = getAdapterPosition();
                final BbcFavItem favItem = favItemList.get(position);
                favDB.deleteArticle(favItem.getId());
                removeItem(position);
            });

    }


    private void removeItem(int position){
        favItemList.remove(position);

//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, favItemList.size());
        }

}