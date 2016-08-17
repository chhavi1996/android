package com.example.android.sunshine.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by chhavi on 5/7/16.
 */
public class TestUtilities extends AndroidTestCase {

    static final String TEST_LOCATION="99705";
    static final long TEST_DATE=1419033600L;//dec.20,2014

    static void ValidateCursor(String error,Cursor valueCursor,ContentValues expectedValues){
        assertTrue("Empty cursor returned:"+error,valueCursor.moveToFirst());
        ValidateCurrentRecord( error, valueCursor,expectedValues);
        valueCursor.close();
    }

    static void ValidateCurrentRecord(String error,Cursor valueCursor,ContentValues expectedValues){
        Set<Map.Entry<String,Object>> valueSet=expectedValues.valueSet();
        for(Map.Entry<String,Object> entry :valueSet){
            String columnName=entry.getKey();
            int index=valueCursor.getColumnIndex(columnName);
            assertFalse(index==-1);

            String expectValue=entry.getValue().toString();
            assertEquals("Error:Does not match the expected value", expectValue, valueCursor.getString(index));
            

        }
    }

    static ContentValues createWeatherValues(long locationRowId){
        ContentValues testValues=new ContentValues();
        testValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY,locationRowId);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_DATE,TEST_DATE);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES,1.1);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY,1.2);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE,1.3);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,75);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,65);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,"Asteroids");
        testValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,5.5);
        testValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,321);

        return testValues;


    }

    static ContentValues createNorthPoleLocationValues(){

        ContentValues testValues=new ContentValues();
        testValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,TEST_LOCATION);
        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME,"North Pole");
        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT,64.7488);
        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG,-147.353);

        return testValues;
    }

    static long insertNorthPoleLocationValues(Context context){
        WeatherDBHelper wdb=new WeatherDBHelper(context);
        SQLiteDatabase db=wdb.getWritableDatabase();

        long locationRowID=db.insert(WeatherContract.LocationEntry.TABLE_NAME,null,TestUtilities.createNorthPoleLocationValues());

        assertTrue( "Error:failure to insert the north pole location values",locationRowID!=-1);

        return locationRowID;
    }
}
