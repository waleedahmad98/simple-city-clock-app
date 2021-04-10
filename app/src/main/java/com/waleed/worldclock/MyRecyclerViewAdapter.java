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

    private ArrayList<Entry> mData; // data array
    private int mSize; // size
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Entry> d) {
        this.context = context;
        this.mData = d;
        this.mInflater = LayoutInflater.from(context);
        this.mSize = d.size();
    }


    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // inflates the cell layout from xml when needed
        View view = mInflater.inflate(R.layout.entry_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // binds the data to the TextView in each cell
        holder.myTextViewName.setText(mData.get(position).getCity_name());
        holder.myTextViewTime.setText(mData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder { // stores and recycles views as they are scrolled off screen
        TextView myTextViewName;
        TextView myTextViewTime;
        ViewHolder(View itemView) {
            super(itemView);
            myTextViewName = itemView.findViewById(R.id.name);
            myTextViewTime = itemView.findViewById(R.id.time);
        }
    }


    public void updateTimes(){ // function to update times
        for (Entry e:mData){
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone(e.getZone_name()));
            e.setTime(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        }
    }
}