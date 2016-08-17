package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class Detail_ActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG=Detail_ActivityFragment.class.getSimpleName();
    private final String FORECAST_STRING_HASHTAG="#SunshineApp";
    private String mforecaststr;
    private ShareActionProvider mshareActionProvider;
    private static final int DETAIL_LOADER=0;

    private static final String[] FORECAST_COLUMNS={

            WeatherContract.WeatherEntry.TABLE_NAME+"."+ WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    static final int COL_WEATHER_ID=0;
    static final int COL_WEATHER_DATE=1;
    static final int COL_WEATHER_DESC=2;
    static final int COL_WEATHER_MAX_TEMP=3;
    static final int COL_WEATHER_MIN_TEMP=4;
    static final int COL_WEATHER_HUMIDITY=5;
    static final int COL_WEATHER_PRESSURE=6;
    static final int COL_WEATHER_WIND_SPEED=7;
    static final int COL_WEATHER_DEGREES=8;
    static final int COL_WEATHER_CONDITION_ID=9;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mPressure;
    private TextView mWindView;
    private TextView mHumidityView;





    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_detail_, container, false);
        mIconView=(ImageView)rootView.findViewById(R.id.icon_imgView);
        mFriendlyDateView=(TextView)rootView.findViewById(R.id.friendly_date_textview);
        mDateView=(TextView)rootView.findViewById(R.id.date_textview);
        mHighTempView=(TextView)rootView.findViewById(R.id.high_temp_textView);
        mLowTempView=(TextView)rootView.findViewById(R.id.low_temp_textView);
        mDescView=(TextView)rootView.findViewById(R.id.desc_textView);
        mHumidityView=(TextView)rootView.findViewById(R.id.humidity_textView);
        mPressure=(TextView)rootView.findViewById(R.id.pressure_textView);
        mWindView=(TextView)rootView.findViewById(R.id.wind_textView);

           return rootView;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment,menu);

            MenuItem menuitem=menu.findItem(R.id.action_share);
        mshareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(menuitem);

        if(mforecaststr!=null){
            mshareActionProvider.setShareIntent(createShareForecastIntent());
        }
        else{
            Log.d(LOG_TAG,"Share Action Provider is null?");
        }
    }

    private  Intent createShareForecastIntent(){

        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,mforecaststr+FORECAST_STRING_HASHTAG);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(DETAIL_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.v(LOG_TAG,"In OnCreateLoader");
        Intent intent=getActivity().getIntent();
        if(intent==null || intent.getData()==null){
            return null;
        }

        return new CursorLoader(getActivity(),intent.getData(),FORECAST_COLUMNS,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(LOG_TAG,"In OnLoadFinished");

        if(data!=null && data.moveToFirst()) {

            int weatherID=data.getInt(COL_WEATHER_CONDITION_ID);

            mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherID));

            long date = data.getLong(COL_WEATHER_DATE);
            String friendlyDate = Utility.getDayName(getActivity(), date);
            String dateView = Utility.getFormattedMonthDay(getActivity(), date);
            mFriendlyDateView.setText(friendlyDate);
            mDateView.setText(dateView);

            String desc = data.getString(COL_WEATHER_DESC);
            mDescView.setText(desc);

            boolean isMetric = Utility.isMetric(getActivity());

            double high = data.getDouble(COL_WEATHER_MAX_TEMP);
            double low = data.getDouble(COL_WEATHER_MIN_TEMP);
            mHighTempView.setText(Utility.formatTemperature(getActivity(), high, isMetric));
            mLowTempView.setText(Utility.formatTemperature(getActivity(), low, isMetric));

            Float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
            mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

            float windSpeedStr = data.getFloat(COL_WEATHER_WIND_SPEED);
            float windDirection = data.getFloat(COL_WEATHER_DEGREES);
            mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirection));

            float pressure = data.getFloat(COL_WEATHER_PRESSURE);
            mPressure.setText(getActivity().getString(R.string.format_pressure, pressure));


//        String date=Utility.formatDate(data.getLong(COL_WEATHER_DATE));
//
//        String desc=data.getString(COL_WEATHER_DESC);
//
//        boolean isMetric=Utility.isMetric(getActivity());
//        String high=Utility.formatTemperature(getActivity(),data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
//        String low=Utility.formatTemperature(getActivity(),data.getDouble(COL_WEATHER_MIN_TEMP),isMetric);

            mforecaststr = String.format("%s - %s - %s/%s", date, desc, high, low);

//        TextView detailTextView=(TextView)getView().findViewById(R.id.detailText);
//        detailTextView.setText(mforecaststr);

            if (mshareActionProvider != null)
                mshareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
