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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class selectorActivity extends AppCompatActivity {
    MyRecyclerViewStringAdapter adapter;
    DBManager DBM = new DBManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        DBM.open();
        if (DBM.fetchAllCities().size() == 0) {
            try {
                setTimeZones();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        ArrayList<String> all_cities_list = DBM.fetchAllCities();
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.save(DBM);
                Toast.makeText(view.getContext(), "Updated Cities!", Toast.LENGTH_SHORT).show();

            }
        });

        RecyclerView recyclerView = findViewById(R.id.all_cities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewStringAdapter(this, all_cities_list, DBM);
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

    public void setTimeZones() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        String[] all_cities = {"Lahore, PK", "Dhaka, BD", "Brussels, BE", "Damascus, SY", "Dubai, AE","Kabul, AF","Sao Tome, ST","Abidjan, CI", "Costa Rica, CR", "Riyadh, SA", "Tokyo, JP", "Kyoto, JP", "Jeddah, SA",  "Karachi, PK", "Islamabad, PK"};
        ArrayList<String> all_cities_list  = new ArrayList<String>();
        String API_KEY = "WHIV0PP6QGP4";
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    JSONParser jsonParser = new JSONParser();
                    URLConnection connection = null; // must handle this exception with a toast notification
                    connection = new URL("https://api.timezonedb.com/v2.1/list-time-zone?key=" + API_KEY + "&format=json").openConnection();
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    InputStream response = connection.getInputStream();
                    JSONObject obj = (JSONObject) jsonParser.parse(new InputStreamReader(response, "UTF-8"));

                    JSONArray zones = (JSONArray) obj.get("zones");

                    for (String s : all_cities){
                        String[] split = s.split(", ");
                        for (int i=0; i < zones.size(); i++){
                            JSONObject index = (JSONObject) zones.get(i);
                            if (split[1].equals(index.get("countryCode").toString())){
                                DBM.insert(split[0] + ", " + index.get("zoneName"));
                                //all_cities_list.add(split[0] + ", " + index.get("zoneName"));
                            }
                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.updateData(DBM.fetchAllCities());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            }).start();
        System.out.println(all_cities);
    }

}