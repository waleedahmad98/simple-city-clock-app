package com.waleed.worldclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
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
    private Context context;
    public ArrayList<String> selected = new ArrayList<String>();
    public boolean restrictor = false;

    MyRecyclerViewStringAdapter(Context context, ArrayList<String> d, DBManager DBM) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = d;
        this.mDataTemp = new ArrayList<String>(mData);
        this.mSize = d.size();
        try {
            load(DBM);
        } catch (Exception e) { }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.selector_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myChkBox.setText(fixText(mData.get(position)));
        holder.myChkBox.setOnCheckedChangeListener(null);

        if (selected.contains(mData.get(position)) && !restrictor) {
                holder.myChkBox.setChecked(true);
        }else{
            holder.myChkBox.setChecked(false);
        }
        holder.myChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked ) {
                    selected.add(mData.get(position));
                } else {
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
    } // filter results

    private Filter filtered = new Filter() {
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
        protected void publishResults(CharSequence constraint, FilterResults results) { // publish filtered results
            mData.clear();
            mData.addAll((ArrayList<String>) results.values);
            mSize = mData.size();
            restrictor = true;
            notifyDataSetChanged();
            restrictor = false;
        }
    };


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox myChkBox;
        ViewHolder(View itemView) {
            super(itemView);
            myChkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void save(DBManager DBM) {
        DBM.update(selected);
    }

    public void load(DBManager DBM) {
        ArrayList<String> temp = DBM.fetch();
        if (!temp.isEmpty()) {
            this.selected.clear();
            this.selected = temp;
        }

    }

    public String fixText(String s) {
        String[] c_name = s.split(", ");
        return c_name[0];
    }
}