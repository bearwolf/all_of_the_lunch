package com.bearwolfapps.allofthelunch;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.bearwolfapps.allofthelunch.data.FilterLunchItems;
import com.bearwolfapps.allofthelunch.data.LunchItems;
import com.bearwolfapps.allofthelunch.data.RestaurantRVAdapter;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    //TextView statusText;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textView;
    TextView visibletxt, colortxt, sizetxt;
    TextView noResult;
    TextView noResult2;
    Button refresh;
    public boolean testPiccolo;
    public boolean testStudio10;
    public boolean testHuoltamo;
    public boolean testIsoPaj;
    public boolean testFazer;
    public String currentTheme;
    public Switch switch1;
    public float magicTextSize;
    private class BackgroundFetchResult{
        boolean success;
        Optional<List<LunchItems.LunchItem>> maybeItems;
        Optional<Exception> maybeException;

        public BackgroundFetchResult(boolean success, List<LunchItems.LunchItem> items, Exception ex){
            this.success = success;
            this.maybeItems = Optional.ofNullable(items);
            this.maybeException = Optional.ofNullable(ex);
        }
    }

    private class BackgroundFetcher extends AsyncTask<String, Void, BackgroundFetchResult>{

        @Override
        protected BackgroundFetchResult doInBackground(String... urls) {
            try {
                List<LunchItems.LunchItem> res = LunchItems.fetchLunchItems(urls[0]);
                return new BackgroundFetchResult(true, res, null);
            }catch(Exception ex){
                return new BackgroundFetchResult(false, null, ex);
            }
        }

        protected void onPostExecute(BackgroundFetchResult res){
            if(res.success){
                refresh = findViewById(R.id.button);
                noResult = findViewById(R.id.noResult);
                noResult2 = findViewById(R.id.noResult2);

                if(res.maybeItems.get().size() == 0){
                    //  statusText.setText("No items returned");

                    refresh.setVisibility(recyclerView.VISIBLE);
                    noResult.setVisibility(View.VISIBLE);
                    noResult2.setVisibility(View.VISIBLE);

                }else {

                    noResult.setVisibility(View.GONE);
                    refresh.setVisibility(recyclerView.GONE);
                    noResult2.setVisibility(View.GONE);
                  //  statusText.setText(String.format(
                    //        "fetched items, %d of them", res.maybeItems.get().size()));
                    String searchText = null;//((TextView)findViewById(R.id.txt_search)).getText().toString();
                    CheckBox veganOnly = findViewById(R.id.cb_veganonly);
                    CheckBox searchInRestaurantNames = findViewById(R.id.cb_search_in_restaurant_name);
                    CheckBox searchInMenuItemNames = findViewById(R.id.cb_search_in_food_name);

                    Boolean piccolo = testPiccolo;
                    Boolean studio10 = testStudio10;
                    Boolean huoltamo = testHuoltamo;
                    Boolean fazer = testFazer;
                    Boolean isopaj = testIsoPaj;
                    List<LunchItems.Restaurant> filteredRestaurants =
                            FilterLunchItems.filterLunchItems(
                                    res.maybeItems.get().get(0).restaurants,
                                    new FilterLunchItems.FilterOptions(searchText,
                                            veganOnly.isChecked(), piccolo == false, studio10 ==false, huoltamo ==false, fazer ==false, isopaj==false,
                                            searchInRestaurantNames.isChecked()==false,
                                            searchInMenuItemNames.isChecked()));
                    RestaurantRVAdapter adapter = new RestaurantRVAdapter(filteredRestaurants);
                    recyclerView.setAdapter(adapter);
                }
            }else {
              //  statusText.setText(String.format(
                 //       "failed fetch, exception %s", res.maybeException.get().toString()));
            }
        }
    }



            // ----------------------------- ONCREATE STARTS HERE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Log.d("Bearwolf",sharedPreferences.getString(getString(R.string.pref_color_key),getString(R.string.pref_theme_apptheme_value)));
        changeTheme(sharedPreferences.getString(getString(R.string.pref_theme_key),getString(R.string.pref_theme_apptheme_value)));
        setContentView(R.layout.activity_main);

        String url = LunchItems.DEFAULT_URL;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        textView = (TextView) findViewById(R.id.textView);
        int myColor = Color.parseColor("#008577");
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(myColor);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // implement Handler to wait for 3 seconds and then update UI means update value of TextView
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // cancle the Visual indication of a refresh
                        swipeRefreshLayout.setRefreshing(false);
                       new BackgroundFetcher().execute(url);
                        Toast.makeText(MainActivity.this, "Hungry?",Toast.LENGTH_LONG).show();
                    }
                }, 500);
            }
        });
      //  Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(mToolbar);
       // statusText = findViewById(R.id.status_text);

       // statusText.setText("fetching from " + url);
       // visibletxt = findViewById(R.id.visible_text);
       // colortxt = findViewById(R.id.color_text);
       // sizetxt = findViewById(R.id.size_text);
        setupSharedPreferences();

       // setTextVisible(sharedPreferences.getBoolean("display_text", true));
        setPiccoloVisible(sharedPreferences.getBoolean("noPiccolo", true));
        setStudio10Visible(sharedPreferences.getBoolean("noStudio", true));
        setHuoltamoVisible(sharedPreferences.getBoolean("noHuoltamo", true));
        setFazerVisible(sharedPreferences.getBoolean("noFazer", true));
        setIsoPajVisible(sharedPreferences.getBoolean("noIsoPaj", true));
        setUtanforDefault(sharedPreferences.getBoolean("alltidUtanfor", false));
       // loadColorFromPreference(sharedPreferences);
       // loadSizeFromPreference(sharedPreferences);
       // Toast.makeText(this, "Hungry?",Toast.LENGTH_LONG).show();
        //SettingsActivity.loadData();
        //SettingsActivity.updateViews();
        //((SettingsActivity)getActivity()).loadData();
       // ((SettingsActivity)getCallingActivity()).updateViews();

        new BackgroundFetcher().execute(url);

        findViewById(R.id.button).setOnClickListener((View v) -> {

            // statusText.setText("fetching from " + url);
            new BackgroundFetcher().execute(url);


        });
        findViewById(R.id.cb_veganonly).setOnClickListener((View v) -> {

           // statusText.setText("fetching from " + url);
            new BackgroundFetcher().execute(url);


        });
        findViewById(R.id.cb_search_in_restaurant_name).setOnClickListener((View v) -> {

          //  statusText.setText("fetching from " + url);
            new BackgroundFetcher().execute(url);


        });
        findViewById(R.id.floatingButton).setOnClickListener((View v) -> {

            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);




        });
        recyclerView = findViewById(R.id.recycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    // Method to set Visibility of Text.
    private void setTextVisible(boolean display_text) {
        if (display_text == true) {
            visibletxt.setVisibility(View.VISIBLE);
        } else {
            visibletxt.setVisibility(View.INVISIBLE);

        }
    }

    private void setPiccoloVisible(boolean noPiccolo) {
        if (noPiccolo == true) {
            testPiccolo = true;
            //new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        } else {
            testPiccolo = false;
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        }
    }
    private void setStudio10Visible(boolean noStudio) {
        if (noStudio == true) {
            testStudio10 = true;
            new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        } else {
            testStudio10 = false;
            new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        }
    }
    private void setHuoltamoVisible(boolean noHuoltamo) {
        if (noHuoltamo == true) {
            testHuoltamo = true;
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        } else {
            testHuoltamo = false;
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        }
    }
    private void setFazerVisible(boolean noFazer) {
        if (noFazer == true) {
            testFazer = true;
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        } else {
            testFazer = false;
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        }
    }
    private void setIsoPajVisible(boolean noIsoPaj) {
        if (noIsoPaj == true) {
            testIsoPaj = true;
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        } else {
            testIsoPaj = false;
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        }
    }
    private void setUtanforDefault(boolean alltidUtanfor) {
        if (alltidUtanfor == true) {
            CheckBox searchInRestaurantNames = findViewById(R.id.cb_search_in_restaurant_name);
            searchInRestaurantNames.setChecked(true);
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        } else {
            CheckBox searchInRestaurantNames = findViewById(R.id.cb_search_in_restaurant_name);
            searchInRestaurantNames.setChecked(false);
           // new BackgroundFetcher().execute(LunchItems.DEFAULT_URL);
        }
    }
    // Method to set Color of Text.
    private void changeTextColor(String pref_color_value) {
        Log.d("Bearwolf", pref_color_value);
        if (pref_color_value.equals("red")) {
            colortxt.setTextColor(Color.RED);
        } else if(pref_color_value.equals("green")) {
            colortxt.setTextColor(Color.GREEN);
        } else {
            colortxt.setTextColor(Color.BLUE);
        }
    }

    private void changeTheme(String pref_theme_value) {
        Log.d("Bearwolf", pref_theme_value);
        if (pref_theme_value.equals("apptheme")) {
            setTheme(R.style.AppTheme);

        } else if(pref_theme_value.equals("comictheme")) {
            setTheme(R.style.ComicTheme);
        }
    }
    // Method to set Size of Text.
    private void changeTextSize(Float i) {
        sizetxt.setTextSize(i);
    }

    // Method to pass value from SharedPreferences
    private void loadColorFromPreference(SharedPreferences sharedPreferences) {
        Log.d("Parzival",sharedPreferences.getString(getString(R.string.pref_color_key),getString(R.string.pref_color_red_value)));
        changeTextColor(sharedPreferences.getString(getString(R.string.pref_color_key),getString(R.string.pref_color_red_value)));
    }

    private void loadSizeFromPreference(SharedPreferences sharedPreferences) {
        float minSize = Float.parseFloat(sharedPreferences.getString(getString(R.string.pref_size_key), "16.0"));
        changeTextSize(minSize);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_about_us) {
            Intent i = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

       // if (key.equals("display_text")) {
        //    setTextVisible(sharedPreferences.getBoolean("display_text", true));
            // testPiccolo = true;
       // } else if (key.equals("color")) {
        //    loadColorFromPreference(sharedPreferences);
     //   } else if (key.equals("size")) {
         //   loadSizeFromPreference(sharedPreferences);
         if (key.equals("noPiccolo")) {
            setPiccoloVisible(sharedPreferences.getBoolean("noPiccolo", true));
        } else if (key.equals("noStudio")) {
            setStudio10Visible(sharedPreferences.getBoolean("noStudio", true));
        } else if (key.equals("noHuoltamo")) {
            setHuoltamoVisible(sharedPreferences.getBoolean("noHuoltamo", true));
        } else if (key.equals("noFazer")) {
            setFazerVisible(sharedPreferences.getBoolean("noFazer", true));
        } else if (key.equals("noIsoPaj")){
            setIsoPajVisible(sharedPreferences.getBoolean("noIsoPaj", true));
        } else if (key.equals("alltidUtanfor")){
            setUtanforDefault(sharedPreferences.getBoolean("alltidUtanfor", false));
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
