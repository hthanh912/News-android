package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ListSearchAdapter extends RecyclerView.Adapter<ListSearchAdapter.ViewHolder> {

    private List<SearchItem> searchItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    ListSearchAdapter(Context context, List<SearchItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.searchItems = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSearchAdapter.ViewHolder holder, int position) {
        SearchItem searchItem =  searchItems.get(position);
        holder.title.setText(searchItem.getTitle());
        holder.description.setText(searchItem.getDesc());
    }
    // binds the data to the TextView in each row

    // total number of rows
    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Page.class);
                    intent.putExtra("link", searchItems.get(getAdapterPosition()).getUrl());
                    context.startActivity(intent);
                }
            });
        }

        public void onClick(View view) {
            if (mClickListener != null){

            }
        }
    }

    // convenience method for getting data at click position
    SearchItem getItem(int id) {
        return searchItems.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}