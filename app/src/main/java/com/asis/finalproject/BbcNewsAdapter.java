package com.asis.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class BbcNewsAdapter extends RecyclerView.Adapter<BbcNewsViewHolder> implements Filterable {
    private Context context;
    private ArrayList<BbcArticles> listArticles;
    private ArrayList<BbcArticles> mArrayList;
    private SqliteDatabase mDatabase;

    BbcNewsAdapter(Context context, final ArrayList<BbcArticles> listArticles) {
        this.context = context;
        this.listArticles = listArticles;
        this.mArrayList = listArticles;
        mDatabase = new SqliteDatabase(context);

    }

    public BbcNewsAdapter(Response.Listener<JSONObject> jsonObjectListener, ArrayList<String> list) {
    }

    @Override
    public BbcNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_article_list_layout, parent, false);
        return new BbcNewsViewHolder(view);

    }
    @Override
    public void onBindViewHolder(BbcNewsViewHolder holder, int position) {
        final BbcArticles articles = listArticles.get(position);
        holder.tvTitle.setText(articles.getTitle());
        holder.tvPubDate.setText(articles.getPubDate());
        holder.tvDescription.setText(articles.getDescription());
        holder.tvUrlLink.setText(articles.getWebUrl());
        holder.addArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog(articles);
            }
        });

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listArticles = mArrayList;
                }
                else {
                    ArrayList<BbcArticles> filteredList = new ArrayList<>();
                    for (BbcArticles articles : mArrayList) {
                        if (articles.getTitle().toLowerCase().contains(charString)) {
                            filteredList.add(articles);
                        }
                    }
                    listArticles = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listArticles;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listArticles = (ArrayList<BbcArticles>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return listArticles.size();
    }

    private void addTaskDialog(final BbcArticles articles){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.bbc_article_list_layout, null);
        final TextView titleText = subView.findViewById(R.id.txtTitle);
        final TextView pubDateText = subView.findViewById(R.id.txtPubDate);
        final TextView descriptionText = subView.findViewById(R.id.txtDescription);
        final TextView webLinkText = subView.findViewById(R.id.link);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to add to favorites? You clicked #" );
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Add to Favorites", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = titleText.getText().toString();
                final String pubDate = pubDateText.getText().toString();
                final String description = descriptionText.getText().toString();
                final String webLink =webLinkText.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(pubDate) || TextUtils.isEmpty(description) || TextUtils.isEmpty(webLink)) {
                    Toast.makeText(context, "Something went wrong. Check your article list", Toast.LENGTH_LONG).show();
                } else {
                    mDatabase.updateArticles(new
                            BbcArticles(Objects.requireNonNull(articles).getId(), title, pubDate, description, webLink));
                    ((Activity) context).finish();
                    context.startActivity(((Activity)
                            context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    //search functionality step 3
    public void filterList(ArrayList<BbcArticles> filteredList){
        listArticles = filteredList;
        notifyDataSetChanged();
    }


}
