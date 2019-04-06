
package com.bearwolfapps.allofthelunch;




import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}


























/*
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.batrachianexcellence.bjoernlunch.DataProcessor;
import com.batrachianexcellence.bjoernlunch.data.PrefKeys;

import static com.batrachianexcellence.bjoernlunch.DataProcessor.SHARED_PREFS;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
   // public static final String SHARED_PREFS = "sharedPrefs";
   // public static final String SWITCH1 = "switch1";
   public Switch switch1;
    public static boolean switchOnOff;
   Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_settings);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar);
        //SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS,0);
        //statusText = findViewById(R.id.status_text);
       // DataProcessor.getBool(PrefKeys.SWITCH1);
        //updateViews();
        //SharedPreferences sharedPreferences = getSharedPreferences()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadData();
        //updateViews();
       // switch1.setChecked(switchOnOff);
        switch1 = (Switch) findViewById(R.id.switch1);
      //  Toast.makeText(SharedPreferences)
        findViewById(R.id.switch1).setOnClickListener((View v) -> {

           // SharedPreferences.Editor  editor = getSharedPreferences(SHARED_PREFS,0)
          //  DataProcessor.setBool(SHARED_PREFS, switch1.isChecked());
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SHARED_PREFS, switch1.isChecked());
            editor.apply();
            //// To Save a value


        });

        }


//    public void saveData(){
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putBoolean(SWITCH1, switch1.isChecked());
//        editor.apply();
//        Toast.makeText(this, "data saved", Toast.LENGTH_SHORT).show();
//    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SHARED_PREFS, false);
    }
    public void updateViews(){
        switch1.setChecked(switchOnOff);
    }
    }



*/
