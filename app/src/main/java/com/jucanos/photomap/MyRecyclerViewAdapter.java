package com.jucanos.photomap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<ThumbnailItem> thumbnailItems;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context,  List<ThumbnailItem> thumbnailItems) {
        this.mInflater = LayoutInflater.from(context);
        this.thumbnailItems = thumbnailItems;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_filter_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView_filter.setImageBitmap(thumbnailItems.get(position).image);
        holder.textView_filter.setText(thumbnailItems.get(position).filterName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return thumbnailItems.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView_filter;
        TextView textView_filter;

        ViewHolder(View itemView) {
            super(itemView);
            imageView_filter = itemView.findViewById(R.id.imageView_filter);
            textView_filter = itemView.findViewById(R.id.textView_filter);
            textView_filter.setElevation((float)10);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ThumbnailItem getItem(int id) {
        return thumbnailItems.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}