package com.example.android.sunshine.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by chhavi on 5/7/16.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG=TestDb.class.getSimpleName();
    long locationRowID;


    public void testCreateDb() throws Throwable{

        final HashSet<String> tableNameHashSet=new HashSet<String>();
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);

        mContext.deleteDatabase(WeatherDBHelper.DATABASE_NAME);
        SQLiteDatabase db=new WeatherDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c=db.rawQuery("Select name from sqlite_master where type='table'",null);

        assertTrue("Error:This means database has not been created correctly",c.moveToFirst());

        do{
            tableNameHashSet.remove(c.getString(0));
        }while(c.moveToNext());

        assertTrue("Error:your database was created without weather and location entry table",tableNameHashSet.isEmpty());

        c=db.rawQuery("PRAGMA table_info ("+ WeatherContract.LocationEntry.TABLE_NAME+")",null);

        assertTrue("Error:This means we were unable to query our database for table info",c.moveToFirst());

        final HashSet<String> locationColumnHashSet=new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);

        int columnNameIndex= c.getColumnIndex("name");

        do{
            String columnName=c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        }while (c.moveToNext());

        assertTrue("Error:the database doesn't contain all of the required location entry column",locationColumnHashSet.isEmpty());

        db.close();

    }

    public void testLocationTable(){

            insertLocation();

    }

    public void testWeatherTable(){

        WeatherDBHelper wdb=new WeatherDBHelper(mContext);
        SQLiteDatabase db=wdb.getWritableDatabase();


        ContentValues testValues=TestUtilities.createWeatherValues(locationRowID);

        long weatherID=db.insert(WeatherContract.WeatherEntry.TABLE_NAME,null,testValues);

        assertTrue(weatherID!=-1);
        Cursor cursor=db.query(WeatherContract.WeatherEntry.TABLE_NAME,null,null,null,null,null,null);

        assertTrue("Error:no records found.",cursor.moveToFirst());

        TestUtilities.ValidateCurrentRecord("Error:Location query validate failed.", cursor, testValues);
        assertFalse("Error: More than one record", cursor.moveToNext());

        cursor.close();
        db.close();

    }

    public long insertLocation(){
        WeatherDBHelper wdb=new WeatherDBHelper(mContext);
        SQLiteDatabase db=wdb.getWritableDatabase();

        ContentValues testValues=TestUtilities.createNorthPoleLocationValues();

        locationRowID=db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);

        assertTrue(locationRowID!=-1);
        Cursor cursor=db.query(WeatherContract.LocationEntry.TABLE_NAME,null,null,null,null,null,null);

        assertTrue("Error:no records found.", cursor.moveToFirst());

        TestUtilities.ValidateCurrentRecord("Error:Location query validate failed.", cursor, testValues);
        assertFalse("Error: More than one record", cursor.moveToNext());

        cursor.close();
        db.close();

        return locationRowID;
    }

}
