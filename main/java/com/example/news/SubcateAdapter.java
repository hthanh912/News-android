package com.example.news;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.news.R.drawable.button_cate_selected;

public class SubcateAdapter extends RecyclerView.Adapter<SubcateAdapter.ViewHolder> {

    private List<Subcate> subcates;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    SubcateAdapter(Context context, List<Subcate> data) {
        this.mInflater = LayoutInflater.from(context);
        this.subcates = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.subcateview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subcate s = subcates.get(position);
        holder.myTextView.setText(s.getName());
        if (s.isSelected) setBtBg(holder.myTextView, true);
        else setBtBg(holder.myTextView, false);
    }

    public void setBtBg(TextView tv, boolean selected){
        if (selected) {
            tv.setBackgroundResource(R.drawable.button_cate_selected);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            tv.setBackgroundResource(R.drawable.button_cate);
            tv.setTextColor(Color.parseColor("#000000"));
        }
        tv.setPadding(dp(5), dp(5), dp(5), dp(5));
    }

    public static int dp(int px) {
        return (int) (px * Resources.getSystem().getDisplayMetrics().density);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return subcates.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.nane);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    Subcate getItem(int id) {
        return subcates.get(id);
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