package com.asis.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * BbcNewsAdapter class
 * Connects data to RecyclerView and determines
 * ViewHolder for displaying the Data
 */
public class BbcNewsAdapter extends RecyclerView.Adapter <BbcNewsAdapter.BbcNewsViewHolder>{
    private Context mContext;
    private ArrayList<BbcArticles> mArticleList;
    private OnItemClickListener mListener;

    /**
     * Constructor of the BbcNewsAdapter
     * @param context
     * @param articleList
     */
    public BbcNewsAdapter(Context context, ArrayList<BbcArticles> articleList) {
        this.mContext = context;
        this.mArticleList = articleList;
    }

    /**
     * OnItemClickListener for usage in RecyclerView
     */
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * BbcNewsAdapter constuctor
     * @param articleList
     */
    public BbcNewsAdapter(ArrayList<BbcArticles> articleList){
       mArticleList = articleList;
    }

    /**
     * This method is called right when the adapter is created and is used to
     * initialize the ViewHolder(s).
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public BbcNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bbc_article_list_layout, parent,false);
        BbcNewsViewHolder evh = new BbcNewsViewHolder(v, mListener);
        return evh;
    }

    /**
     * This method is called for each ViewHolder to bind it to the adapter.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BbcNewsViewHolder holder, int position) {
        BbcArticles currentItem = mArticleList.get(position);
        holder.mTitle.setText(currentItem.getTitle());
        holder.mPubDate.setText(currentItem.getPubDate());
        holder.mDescription.setText(currentItem.getDescription());
        holder.mWebLink.setText(currentItem.getWebUrl());
    }

    /**
     * Returns the size of the collection that contains the items we want to display
     * @return
     */
    @Override
    public int getItemCount() {
        if (mArticleList == null)
            return 0;
        else
            return mArticleList.size();
    }

    /**
     * BbcNewsViewHolder  class
     * Holds the article’s views
     */
    public static class BbcNewsViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public TextView mPubDate;
        public TextView mDescription;
        public TextView mWebLink;

        /**
         * BbcNewsViewHolder Constructor
         * @param itemView
         * @param listener
         */
        public BbcNewsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.txtTitle);
            mPubDate=itemView.findViewById(R.id.txtPubDate);
            mDescription = itemView.findViewById(R.id.txtDescription);
            mWebLink = itemView.findViewById(R.id.link);
            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * Allows to click on items in RecyclerView
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     *  Article search functionality step 3
     */

    public void filterList(ArrayList<BbcArticles> filteredList){
        mArticleList = filteredList;
        notifyDataSetChanged();
    }

}





