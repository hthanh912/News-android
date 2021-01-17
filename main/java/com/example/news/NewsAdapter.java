package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Article> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    int title_padding;
    // data is passed into the constructor
    NewsAdapter(Context context, List<Article> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.article_view, parent, false);
        title_padding = dp(8);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article s = items.get(position);
        if (s.desc != ""){
            holder.desc.setVisibility(View.VISIBLE);
            holder.layout.setOrientation(LinearLayout.VERTICAL);
            holder.thumb.setMaxWidth(((LinearLayout)holder.thumb.getParent()).getWidth());
            holder.desc.setText(s.getDesc());
            holder.title.setTextSize(20);
            holder.title.setPadding(0, 0, 0, 0);
        }
        else{
            holder.desc.setVisibility(View.GONE);
            holder.layout.setOrientation(LinearLayout.HORIZONTAL);
            holder.title.setTextSize(15);
            holder.title.setPadding(0, 0, title_padding, 0);
        }

        holder.title.setText(s.getTitle());

        holder.cate.setText(s.getCate());
        holder.time.setText(s.getTime());

        if (holder.cate.getText().toString()=="") holder.line.setVisibility(View.GONE);
        else holder.line.setVisibility(View.VISIBLE);

        if (holder.cate.getText().toString()!="" || holder.time.getText().toString()!="") {
            holder.info.setVisibility(View.VISIBLE);
        }
        else if (holder.cate.getText().toString()=="" && holder.time.getText().toString()=="")
            holder.info.setVisibility(View.GONE);

        if (items.get(position).getLink() != "") {
            Picasso.get().load(items.get(position).getThumb()).placeholder(R.drawable.placeholder_thumb).into(holder.thumb);
        }
    }
    public static int dp(int px) {
        return (int) (px * Resources.getSystem().getDisplayMetrics().density);
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return items.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, time, cate, line;
        ImageView thumb;
        LinearLayout info, layout;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            thumb = itemView.findViewById(R.id.thumb);
            time = itemView.findViewById(R.id.time);
            cate = itemView.findViewById(R.id.cate);
            line = itemView.findViewById(R.id.line);
            info = itemView.findViewById(R.id.info);
            layout = itemView.findViewById(R.id.layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Page.class);
                    intent.putExtra("link", "https://dantri.com.vn" + items.get(getAdapterPosition()).getLink());
                    context.startActivity(intent);
                }
            });
        }

        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    Article getItem(int id) {
        return items.get(id);
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