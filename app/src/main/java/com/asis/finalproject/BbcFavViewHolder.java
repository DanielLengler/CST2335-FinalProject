package com.asis.finalproject;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BbcFavViewHolder extends RecyclerView.ViewHolder{
        TextView favTitleView, favPubDateView, favDescriptionView, favLinkView;
                Button favBtn;
        public BbcFavViewHolder(@NonNull View itemView) {
            super(itemView);
            favTitleView = itemView.findViewById(R.id.artTitle);
            favPubDateView = itemView.findViewById(R.id.artPubDate);
            favDescriptionView = itemView.findViewById(R.id.artDescription);
            favLinkView = itemView.findViewById(R.id.artLink);
            favBtn = itemView.findViewById(R.id.favBtn);
            //removes from favorite list after click
//            favBtn.setOnClickListener((view) -> {
//                int position = getAdapterPosition();
//                final BbcFavItem favItem = favItemList.get(position);
//                favDB.deleteArticle(favItem.getId());
//                removeItem(position);
//            });

        }
//    private void removeItem(int position){
//        favItemList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, favItemList.size());
//    }
    }