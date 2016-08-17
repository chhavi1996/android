package com.example.android.sunshine.app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Vector;

import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.data.WeatherContract.WeatherEntry;

/**
 * Created by chhavi on 16/7/16.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
//        private ArrayAdapter<String> mForecastAdapter;
        private final Context mContext;

    public FetchWeatherTask(Context context) {
        mContext=context;
    }

    private boolean DEBUG=true;


//    private String getReadableDateString(long time){
//
//            Date d=new Date(time);
//            SimpleDateFormat date=new SimpleDateFormat("E, MMM d");
//            return date.format(date).toString();
//        }
//
//        private String formathighlows(double high,double low){
//
//           SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(mContext);
//            String unitType=prefs.getString(mContext.getString(R.string.pref_unit_key),mContext.getString(R.string.pref_unit_metric));
//
//
//            if(unitType.equals(mContext.getString(R.string.pref_unit_imperial))){
//                high=(high*1.8)+32;
//                low=(low*1.8)+32;
//            } else if(!unitType.equals(mContext.getString(R.string.pref_unit_metric))){
//                Log.v(LOG_TAG, "Unit type not found " + unitType);
//            }
//
//            long roundedHigh=Math.round(high);
//            long roundedLow=Math.round(low);
//
//            String highLowStr=roundedHigh+"/"+roundedLow;
//
//            return highLowStr;
//        }

        long addLocation(String locationSetting,String cityName,double lat,double lon){

            long locationID;

            Cursor locationCursor=mContext.getContentResolver().query(WeatherContract.LocationEntry.CONTENT_URI,
                    new String[]{WeatherContract.LocationEntry._ID},
                    WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + "=?",
                    new String[]{locationSetting},
                    null);

            if(locationCursor.moveToFirst()){
                int locationIDindex=locationCursor.getColumnIndex(WeatherContract.LocationEntry._ID);
                locationID=locationCursor.getLong(locationIDindex);
            }
            else{

                ContentValues values=new ContentValues();
                values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME,cityName);
                values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,locationSetting);
                values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT,lat);
                values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG,lon);

                Uri locationUri=mContext.getContentResolver().insert(WeatherContract.LocationEntry.CONTENT_URI,values);

                locationID= ContentUris.parseId(locationUri);
            }

            locationCursor.close();
            return locationID;
        }

//        String[] convertContentValuestoUXFormat(Vector<ContentValues> cvv)
//        {
//            String[] resultStr=new String[cvv.size()];
//
//
//            for(int i=0;i<cvv.size();i++) {
//
//                ContentValues weatherValues=cvv.elementAt(i);
//
//                String highlows = formathighlows(weatherValues.getAsDouble(WeatherEntry.COLUMN_MAX_TEMP),
//                        weatherValues.getAsDouble(WeatherEntry.COLUMN_MIN_TEMP));
//
//                resultStr[i]=getReadableDateString(weatherValues.getAsLong(WeatherEntry.COLUMN_DATE))+"-"+weatherValues.getAsString(WeatherEntry.COLUMN_SHORT_DESC)
//                        +"-"+highlows;
//
//            }
//
//            return  resultStr;
//        }

        private void getWeatherDataFromJson(String forecastjsonStr,String locationSettings) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.

            // Location information
            final String CITY="city";
            final String CITY_NAME="name";
            final String COORD="coord";

            //Location Coordinate
            final String LONGITUDE="lon";
            final String LATITUDE="lat";

            //Weather Information
            final String LIST="list";

            final String PRESSURE="pressure";
            final String HUMIDITY="humidity";
            final String WIND_SPEED="speed";
            final String WIND_DIRECTION="deg";

            //All temperatures are children of the "temp" object.

            final String WEATHER="weather";
            final String TEMPERATURE="temp";
            final String MAX="max";
            final String MIN="min";
            final String DESCRIPTION="main";
            final String WEATHER_ID="id";

            try {
                JSONObject forecastjson = new JSONObject(forecastjsonStr);
                JSONArray weatherArray = forecastjson.getJSONArray(LIST);

                JSONObject cityJson=forecastjson.getJSONObject(CITY);
                String cityName=cityJson.getString(CITY_NAME);

                JSONObject coord=cityJson.getJSONObject(COORD);
                double lat=coord.getDouble(LATITUDE);
                double lon=coord.getDouble(LONGITUDE);

                long locationId=addLocation(locationSettings, cityName, lat, lon);

                Vector<ContentValues> cVVector=new Vector<ContentValues>(weatherArray.length());

                Time daytime = new Time();

                daytime.setToNow();

                int juliandayStart = Time.getJulianDay(System.currentTimeMillis(), daytime.gmtoff);

                daytime = new Time();

                for (int i = 0; i < weatherArray.length(); i++) {

                    long dateTime;
                    double pressure;
                    int humidity;
                    double windspeed;
                    double windDirection;

                    double high;
                    double low;


                    String description;
                    int weather_id;

                    JSONObject dayForecast = weatherArray.getJSONObject(i);

                    dateTime = daytime.setJulianDay(juliandayStart + i);
                    pressure=dayForecast.getDouble(PRESSURE);
                    humidity=dayForecast.getInt(HUMIDITY);
                    windspeed=dayForecast.getDouble(WIND_SPEED);
                    windDirection=dayForecast.getDouble(WIND_DIRECTION);

                    JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
                    description = weatherObject.getString(DESCRIPTION);
                    weather_id=weatherObject.getInt(WEATHER_ID);

                    JSONObject tempObject = dayForecast.getJSONObject(TEMPERATURE);
                     high = tempObject.getDouble(MAX);
                     low = tempObject.getDouble(MIN);

                    ContentValues weatherValues=new ContentValues();

                    weatherValues.put(WeatherEntry.COLUMN_LOC_KEY,locationId);
                    weatherValues.put(WeatherEntry.COLUMN_DATE,dateTime);
                    weatherValues.put(WeatherEntry.COLUMN_HUMIDITY,humidity);
                    weatherValues.put(WeatherEntry.COLUMN_PRESSURE,pressure);
                    weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED,windspeed);
                    weatherValues.put(WeatherEntry.COLUMN_DEGREES,windDirection);
                    weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP,high);
                    weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP,low);
                    weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC,description);
                    weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID,weather_id);

                    cVVector.add(weatherValues);
                }

                int inserted=0;

                //add to database
                if(cVVector.size()>0){

                    ContentValues[] cvArray=new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
//                    mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI,cvArray);
//
//                }
//
//                //sort by date
//
//                String sortorder=WeatherEntry.COLUMN_DATE +" ASC";
//                Uri weatherforLocationUri=WeatherEntry.buildWeatherLocationWithStartDate(locationSettings,System.currentTimeMillis());
//
//                //to display what we have stored in the bulkinsert
//
//                Cursor cur=mContext.getContentResolver().query(WeatherEntry.CONTENT_URI,null,null,null,sortorder);
//
//                cVVector=new Vector<ContentValues>(cur.getCount());
//
//                if(cur.moveToFirst()) {
//                    do {
//                        ContentValues cv = new ContentValues();
//
//                        DatabaseUtils.cursorRowToContentValues(cur, cv);
//                        cVVector.add(cv);
//                    } while (cur.moveToNext());
                        inserted=mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI,cvArray);
                }

//                Log.d(LOG_TAG,"Fetch weatherTask complete."+cVVector.size()+" Inserted!");
//
//                String[] ResultStr=convertContentValuestoUXFormat(cVVector);

                Log.d(LOG_TAG,"Fetch Weather Task completed."+inserted+" Inserted");

//                return ResultStr;
            }
            catch (JSONException e){

                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();

            }


        }



        @Override
        protected Void doInBackground(String... params) {

            if(params.length==0){
                return null;
            }

            String locationQuery=params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr=null;

            String format="json";
            String units="metric";
            int numdays=14;


            try {
                final String FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM="q";
                final String FORMAT_PARAM="mode";
                final String UNITS_PARAM="units";
                final String DAYS_PARAM="cnt";
                final String APPID_PARAM="appid";

                Uri builtUri=Uri.parse(FORECAST_BASE_URL).buildUpon()
                            .appendQueryParameter(QUERY_PARAM,params[0])
                            .appendQueryParameter(FORMAT_PARAM,format)
                            .appendQueryParameter(UNITS_PARAM,units)
                            .appendQueryParameter(DAYS_PARAM,Integer.toString(numdays))
                            .appendQueryParameter(APPID_PARAM,BuildConfig.OPEN_WEATHER_MAP_API_KEY).build();

                URL url=new URL(builtUri.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                forecastJsonStr = buffer.toString();

                getWeatherDataFromJson(forecastJsonStr,locationQuery);

            }catch (IOException e){
                Log.d(LOG_TAG,"Error,e");

            }
            catch (JSONException e) {

                Log.e(LOG_TAG,e.getMessage(), e);
                e.printStackTrace();

//                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();

                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "error closing stream", e);
                    }
                }
            }

//            try{
//                return getWeatherDataFromJson(forecastJsonStr,locationQuery);
//            }
//            catch (JSONException e){
//                Log.e(LOG_TAG,e.getMessage(),e);
//                e.printStackTrace();
//            }
//
            return null;

        }

//        @Override
//        protected void onPostExecute(String[] result) {
//
//            if(result!=null && mForecastAdapter!=null){
//                mForecastAdapter.clear();
//                for(String dayforecst:result){
//                    mForecastAdapter.add(dayforecst);
//                }
//            }
//        }
}
