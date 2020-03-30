package com.asis.finalproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BbcNewsFavoriteViewHolder extends RecyclerView.ViewHolder {
    TextView favTitle, favPubDate, favDescription, favUrlLink;
    ImageView deleteArticle;

    BbcNewsFavoriteViewHolder(View itemView) {
        super(itemView);
        favTitle = itemView.findViewById(R.id.artTitle);
        favPubDate = itemView.findViewById(R.id.artPubDate);
        favDescription = itemView.findViewById(R.id.artDescription);
        favUrlLink = itemView.findViewById(R.id.artLink);
        deleteArticle = itemView.findViewById(R.id.deleteArticle);

    }

}
