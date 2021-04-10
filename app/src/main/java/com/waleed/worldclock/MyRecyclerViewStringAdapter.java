package com.waleed.worldclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MyRecyclerViewStringAdapter extends RecyclerView.Adapter<MyRecyclerViewStringAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> mData;
    private ArrayList<String> mDataTemp;
    private int mSize;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    public ArrayList<String> selected = new ArrayList<String>();

    // data is passed into the constructor
    MyRecyclerViewStringAdapter(Context context, ArrayList<String> d) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = d;
        this.mDataTemp = new ArrayList<String>(mData);
        this.mSize = d.size();
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.selector_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myChkBox.setText(fixText(mData.get(position)));
        holder.myChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selected.add(mData.get(position));
                }else {
                    selected.remove(mData.get(position));
                }
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mSize;
    }

    @Override
    public Filter getFilter() {
        return filtered;
    }

    private Filter filtered = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredList = new ArrayList<String>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mDataTemp);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String s : mDataTemp) {
                    if (s.toLowerCase().contains(filterPattern)) {
                        filteredList.add(s);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData.clear();
            mData.addAll((ArrayList<String>) results.values);
            mSize = mData.size();
            notifyDataSetChanged();
        }
    };


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox myChkBox;
        ViewHolder(View itemView) {
            super(itemView);
            myChkBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
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

    public void save(Context context){
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Set<String> set = new HashSet<String>(this.selected);
        editor.putStringSet("Selected", set);
        editor.commit();
    }
    public void load(Context context){
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        Set<String> set = pref.getStringSet("Selection", null);
        if (!set.isEmpty()){
            this.selected.clear();
            this.selected = new ArrayList<String>(set);
        }

    }

    public String fixText(String s){
        String[] c_name = s.split(", ");
        return c_name[0];
    }
}