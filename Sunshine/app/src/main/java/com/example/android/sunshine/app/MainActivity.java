package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.sunshine.app.data.WeatherDBHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG=MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG="DFTAG";
    private String mLocation;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mLocation=Utility.getPreferredLocation(this);

        setContentView(R.layout.activity_main);


        if(findViewById(R.id.weather_detail_container)!=null){

            mTwoPane=true;

            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container,new Detail_ActivityFragment(),DETAILFRAGMENT_TAG).commit();
            }
        }
        else{
            mTwoPane=false;
        }




    }

    @Override
    protected void onResume() {
        super.onResume();

        String location=Utility.getPreferredLocation(this);

        if(location!=null && !location.equals(mLocation)){
            ForecastFragment ff=(ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);

            if(ff!=null)
                    ff.onLocationChanged();

        }

        mLocation=location;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
       

        if(id==R.id.action_map){
            openPreferredLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap(){

//        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
//        String location=prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        String location=Utility.getPreferredLocation(this);

        Uri geolocation=Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q",location).build();

        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(geolocation);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        else{
            Log.d(LOG_TAG,"couldn't call"+location+",no receiving apps installed");
        }
    }
}
