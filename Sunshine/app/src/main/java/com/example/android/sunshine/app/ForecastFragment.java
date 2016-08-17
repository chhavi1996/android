package com.example.android.sunshine.app;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.android.sunshine.app.data.WeatherContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int FORECAST_LOADER=0;

    private static final String[] FORECAST_COLUMNS={

            WeatherContract.WeatherEntry.TABLE_NAME+"."+ WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    static final int COL_WEATHER_ID=0;
    static final int COL_WEATHER_DATE=1;
    static final int COL_WEATHER_DESC=2;
    static final int COL_WEATHER_MAX_TEMP=3;
    static final int COL_WEATHER_MIN_TEMP=4;
    static final int COL_LOCATION_SETTINGS=5;
    static final int COL_WEATHER_CONDITION_ID=6;
    static final int COL_COORD_LAT=7;
    static final int COL_COORD_LONG=8;




    private ForecastAdapter mForecastAdapter;

    public ForecastFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id==R.id.action_refresh){

           updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   void onLocationChanged(){

       updateWeather();
       getLoaderManager().restartLoader(FORECAST_LOADER,null,this);
   }

    private void updateWeather(){

        FetchWeatherTask weatherTask=new FetchWeatherTask(getActivity());

        String location=Utility.getPreferredLocation(getActivity());
//        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String location=prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        weatherTask.execute(location);
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootView!=null){
            ViewGroup parent=(ViewGroup)rootView.getParent();
            if(parent!=null){
                parent.removeView(rootView);
            }
        }
        try{
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

        }
        catch(InflateException e){

        }
//        List<String> weatherforecast = new ArrayList<String>();
//
//        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weatherforecast);
        String locationSetting=Utility.getPreferredLocation(getActivity());

        //sort order:Ascending by date

        String sortOrder= WeatherContract.WeatherEntry.COLUMN_DATE +" ASC";
        Uri weatherforLocationUri= WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting,System.currentTimeMillis());

        Cursor cur=getActivity().getContentResolver().query(weatherforLocationUri,null,null,null,sortOrder);

        // The CursorAdapter will take data from our cursor and populate the ListView
        // However, we cannot use FLAG_AUTO_REQUERY since it is deprecated, so we will end
        // up with an empty list the first time we run.

        mForecastAdapter=new ForecastAdapter(getActivity(),cur,0);


        // Get a reference to the ListView, and attach this adapter to it.
        ListView list = (ListView) rootView.findViewById(R.id.listview_forecast);
        list.setAdapter(mForecastAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Cursor cur=(Cursor)parent.getItemAtPosition(position);
                String locationSetting=Utility.getPreferredLocation(getActivity());
                Intent intent=new Intent(getActivity(),Detail_Activity.class)
                        .setData(WeatherContract.WeatherEntry.buildWeatherLocationwithdate(locationSetting,cur.getLong(COL_WEATHER_DATE)));
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(FORECAST_LOADER,null,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String locationSetting=Utility.getPreferredLocation(getActivity());

        String sortOrder= WeatherContract.WeatherEntry.COLUMN_DATE+" ASC";
        Uri weatherforLocationUri= WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting,System.currentTimeMillis());

        return new CursorLoader(getActivity(),weatherforLocationUri,FORECAST_COLUMNS,null,null,sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mForecastAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mForecastAdapter.swapCursor(null);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ForecastFragment ff=(ForecastFragment) getFragmentManager().findFragmentById(R.id.fragment_forecast);

        if(ff!=null){

            getFragmentManager().beginTransaction().remove(ff).commit();
        }
    }
}