package com.waleed.worldclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class selectorActivity extends AppCompatActivity {
    MyRecyclerViewStringAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        String[] all_cities = {"Lahore, Asia/Karachi", "New York, America/New_York", "Madrid, Europe/Madrid", "Dhaka, Asia/Dhaka", "Sydney, Australia/Sydney", "Dubai, Asia/Dubai","Paris, Europe/Paris","Sao Tome, Africa/Sao_Tome","Abidjan, Africa/Abidjan", "Dominica, America/Dominica", "Riyadh, Asia/Riyadh", "Tokyo, Asia/Tokyo", "Eucla, Australia/Eucla", "Santiago, America/Santiago", "Metlakatla, America/Metlakatla", "Karachi, Asia/Karachi", "Islamabad, Asia/Karachi"};
        ArrayList<String> all_cities_list  = new ArrayList<String>();
        Collections.addAll(all_cities_list, all_cities);
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.save(view.getContext());
                Toast.makeText(view.getContext(), "Updated Cities!", Toast.LENGTH_SHORT).show();

            }
        });

        RecyclerView recyclerView = findViewById(R.id.all_cities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewStringAdapter(this, all_cities_list);
        recyclerView.setAdapter(adapter);

    }

    public void onBackPressed(){ // load main activity on back press
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}