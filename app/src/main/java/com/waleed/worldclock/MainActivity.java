package com.waleed.worldclock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.waleed.worldclock.Entry;
import com.waleed.worldclock.MyRecyclerViewAdapter;
import com.waleed.worldclock.R;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;
    public ArrayList<Entry> cities = new ArrayList<Entry>();
    Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            cities.addAll(this.load(this));
        }
        catch (Exception e){

        }
        finally {
            RecyclerView recyclerView = findViewById(R.id.rvNumbers);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyRecyclerViewAdapter(this, cities);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, selectorActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            recyclerView.setAdapter(adapter);
            timerHandler = new Handler();
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            adapter.updateTimes();
            adapter.notifyDataSetChanged();
            timerHandler.postDelayed(this, 1000); //run every second
        }
    };

    public ArrayList<Entry> load(Context context){
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        Set<String> set = pref.getStringSet("Selected", null);
        ArrayList<Entry> temp = new ArrayList<Entry>();
        for (String e:set){
            Calendar cal = Calendar.getInstance();
            String[] true_name = e.split(", ");
            cal.setTimeZone(TimeZone.getTimeZone(true_name[1]));
            temp.add(new Entry(true_name[0],true_name[1],cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) ));
        }
        return temp;
    }


}




