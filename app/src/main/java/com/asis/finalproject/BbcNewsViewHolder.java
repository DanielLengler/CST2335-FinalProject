package com.asis.finalproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BbcNewsViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvPubDate, tvDescription, tvUrlLink;
    ImageView addArticle;

    //    ImageView constructor;
    BbcNewsViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.txtTitle);
        tvPubDate = itemView.findViewById(R.id.txtPubDate);
        tvDescription = itemView.findViewById(R.id.txtDescription);
        tvUrlLink = itemView.findViewById(R.id.link);
        addArticle = itemView.findViewById(R.id.addArticle);
    }

}
