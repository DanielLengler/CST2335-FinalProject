package com.asis.finalproject;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class BbcAdapter extends RecyclerView.Adapter<BbcAdapter.BbcViewHolder> {
    private ArrayList<BbcItem> bbcItems;
    private Context context;
    private BbcFavDB favDB;


    public BbcAdapter(ArrayList<BbcItem> articleList, Context context) {
        this.bbcItems = articleList;
        this.context = context;
        favDB = new BbcFavDB(context);
    }

    /**
     * BbcNewsAdapter constuctor
     * @param articleList
     */
    public BbcAdapter(ArrayList<BbcItem> articleList){
        bbcItems = articleList;
    }


    @NonNull
    @Override
    public BbcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_item, parent, false);
        return new BbcViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BbcViewHolder holder, int position) {
        final BbcItem bbcItem = bbcItems.get(position);
        holder.titleTextView.setText(bbcItem.getTitle());
        holder.pubDateTextView.setText(bbcItem.getPubDate());
        holder.descriptionTextView.setText(bbcItem.getDescription());
        holder.linkTextView.setText(bbcItem.getWebUrl());
        holder.favBtn.setOnClickListener((view) -> {
            addTaskDialog();
        });
    }


    @Override
    public int getItemCount() {
        if (bbcItems == null)
            return 0;
        else
            return bbcItems.size();
    }


    public class BbcViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, pubDateTextView, descriptionTextView, linkTextView;
        Button favBtn;

        public BbcViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txtTitle);
            pubDateTextView = itemView.findViewById(R.id.txtPubDate);
            descriptionTextView = itemView.findViewById(R.id.txtDescription);
            linkTextView = itemView.findViewById(R.id.link);
            favBtn = itemView.findViewById(R.id.favBtn);

        }

//    private void createTableOnFirstStart(){
//        favDB.insertEmpty();
//
//        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("firstStart", false);
//        editor.apply();
//    }
//
//    private void readCursorData(BbcItem bbcItem, BbcNewsViewHolder bbcNewsViewHolder){
//        Cursor cursor = favDB.read_all_data(bbcItem.getKey_id());
//        SQLiteDatabase db = favDB.getReadableDatabase();
//        try{
//            while (cursor.moveToNext()){
//                String item_fav_status = cursor.getString(cursor.getColumnIndex(BbcFavDB.FAVORITE_STATUS));
//                bbcItem.setFavStatus(item_fav_status);
//
//                //check fav status
//                if (item_fav_status != null && item_fav_status.equals("1")){
//                    bbcNewsViewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_color);
//                } else if (item_fav_status != null && item_fav_status.equals("0")){
//                    bbcNewsViewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_border);
//                }
//            }
//        }finally{
//            if(cursor != null && cursor.isClosed())
//                cursor.close();
//            db.close();
//        }
//    }



    }
    /**
     * Article search functionality step 3
     */

    public void filterList(ArrayList<BbcItem> filteredList) {
        bbcItems = filteredList;
        notifyDataSetChanged();
    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.bbc_article_list_layout, null);
        final TextView titleText = subView.findViewById(R.id.txtTitle);
        final TextView pubDateText = subView.findViewById(R.id.txtPubDate);
        final TextView descriptionText = subView.findViewById(R.id.txtDescription);
        final TextView webLinkText = subView.findViewById(R.id.link);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to add to favorites? ");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Add to Favorites", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = titleText.getText().toString();
                final String pubDate = pubDateText.getText().toString();
                final String description = descriptionText.getText().toString();
                final String webLink = webLinkText.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(pubDate) || TextUtils.isEmpty(description) || TextUtils.isEmpty(webLink)) {
                    Toast.makeText(context, "Something went wrong. Check your article list", Toast.LENGTH_LONG).show();
                } else {
//                    BbcItem newBbcItem = new BbcItem(title, pubDate, description, webLink);
//                    favDB.updateArticles(newBbcItem);
                    BbcFavItem newFavItem = new BbcFavItem(title, pubDate, description, webLink);
                    favDB.addArticles(newFavItem);
                    ((Activity) context).finish();
                    context.startActivity(((Activity)
                            context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}





