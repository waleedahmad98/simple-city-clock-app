package com.waleed.worldclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Entry> mData;
    private int mSize;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Entry> d) {
        this.context = context;
        this.mData = new ArrayList<Entry>(d);
        this.mInflater = LayoutInflater.from(context);
        this.mSize = d.size();
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.entry_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextViewName.setText(mData.get(position).getCity_name());
        holder.myTextViewTime.setText(mData.get(position).getTime());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mSize;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewName;
        TextView myTextViewTime;
        ViewHolder(View itemView) {
            super(itemView);
            myTextViewName = itemView.findViewById(R.id.name);
            myTextViewTime = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Entry getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateTimes(){
        for (Entry e:mData){
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone(e.getZone_name()));
            e.setTime(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        }
    }
}