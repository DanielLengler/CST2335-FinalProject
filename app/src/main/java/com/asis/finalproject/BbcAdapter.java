package com.asis.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * BbcAdapter class
 * Connects data to RecyclerView and determines
 * ViewHolder for displaying the Data
 */
public class BbcAdapter extends RecyclerView.Adapter<BbcAdapter.BbcViewHolder> {
    /**
     * ArrayList of BBC articles loaded from the Internet
     */
    private ArrayList<BbcItem> bbcItems;
    /**
     * Content of the articles ArrayList
     */
    private Context context;
    /**
     * database of favorite Articles
     */
    private BbcFavDB favDB;

    /**
     * BbcAdapter Constructor that includes:
     * @param articleList ArrayList of articles
     * @param context content of the articles
     */
    public BbcAdapter(ArrayList<BbcItem> articleList, Context context) {
        this.bbcItems = articleList;
        this.context = context;
        favDB = new BbcFavDB(context);
    }

    /**
     * Constructor
     * @param articleList ArrayList of articles
     */
    public BbcAdapter(ArrayList<BbcItem> articleList){
        bbcItems = articleList;
    }

    /**
     * This method calls for creation of a new ViewHolder
     * @param parent is a grouping parameter
     * @param viewType initializes the layout
     * @return a new ViewHolder object
     */
    @NonNull
    @Override
    public BbcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_item, parent, false);
        return new BbcViewHolder(view);
    }

    /**
     * This binds content (data) with views of RecyclerView
     * @param holder holds position and content of the item together
     * @param position identifies position of each element
     */
    @Override
    public void onBindViewHolder(@NonNull BbcViewHolder holder, int position) {
        final BbcItem bbcItem = bbcItems.get(position);
        holder.titleTextView.setText(bbcItem.getTitle());
        holder.pubDateTextView.setText(bbcItem.getPubDate());
        holder.descriptionTextView.setText(bbcItem.getDescription());
        holder.linkTextView.setText(bbcItem.getWebUrl());
        holder.favBtn.setOnClickListener((view) -> {
            addTaskDialog(bbcItem);
        });
    }

    /**
     * Returns the total number of items held by adapter
     * @return the size of items in the arrayList
     */
    @Override
    public int getItemCount() {
        if (bbcItems == null)
            return 0;
        else
            return bbcItems.size();
    }

    /**
     * This inner class BbcViewHolder describes an item view and its location
     * within the RecyclerView
     */
    public class BbcViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, pubDateTextView, descriptionTextView, linkTextView;
        Button favBtn;

        /**
         * Constructor of BbcViewHolder class
         * @param itemView gives references to the view of loaded articles in rows
         */
        public BbcViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txtTitle);
            pubDateTextView = itemView.findViewById(R.id.txtPubDate);
            descriptionTextView = itemView.findViewById(R.id.txtDescription);
            linkTextView = itemView.findViewById(R.id.link);
            favBtn = itemView.findViewById(R.id.favBtn);

        }
    }
    /**
     * Article search functionality step 3
     */

    public void filterList(ArrayList<BbcItem> filteredList) {
        bbcItems = filteredList;
        notifyDataSetChanged();
    }
    /**
     * Alert Dialog for adding an article to the favorite list database
     */
    private void addTaskDialog(BbcItem bbcItem) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.bbc_article_list_layout, null);
        final TextView titleText = subView.findViewById(R.id.txtTitle);
        final TextView pubDateText = subView.findViewById(R.id.txtPubDate);
        final TextView descriptionText = subView.findViewById(R.id.txtDescription);
        final TextView webLinkText = subView.findViewById(R.id.link);

        titleText.setText(bbcItem.getTitle());
        pubDateText.setText(bbcItem.getPubDate());
        descriptionText.setText(bbcItem.getDescription());
        webLinkText.setText(bbcItem.getWebUrl());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.alert_title);
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton(R.string.alert_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    BbcFavItem newFavItem = new BbcFavItem(bbcItem.getTitle(), bbcItem.getPubDate(), bbcItem.getDescription(), bbcItem.getWebUrl());
                    favDB.addArticles(newFavItem);
                    ((Activity) context).finish();
                    context.startActivity(((Activity)
                            context).getIntent());

            }
        });
        builder.setNegativeButton(R.string.alert_negative_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, R.string.alert_cancel_toast, Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}





