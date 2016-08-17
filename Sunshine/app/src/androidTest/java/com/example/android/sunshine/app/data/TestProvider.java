package com.example.android.sunshine.app.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.test.AndroidTestCase;

/**
 * Created by chhavi on 12/7/16.
 */
public class TestProvider extends AndroidTestCase {

    public void testProviderRegistry(){

        PackageManager pm=mContext.getPackageManager();

        ComponentName cm=new ComponentName(mContext.getPackageName(),WeatherProvider.class.getName());
        try{

            ProviderInfo providerInfo=pm.getProviderInfo(cm,0);

            assertEquals("Error:WeatherProvider registered with authority"+providerInfo.authority+" instead of"+WeatherContract.CONTENT_AUTHORITY,providerInfo.authority,WeatherContract.CONTENT_AUTHORITY);
        }catch (PackageManager.NameNotFoundException e){
             assertTrue("Error: WeatherProvider not registered at"+mContext.getPackageName(),false);
        }
    }

    public void testgetType(){

        String type=mContext.getContentResolver().getType(WeatherContract.WeatherEntry.CONTENT_URI);
        assertEquals("Error:WeatherEntry CONTENT_URI should return WeatherEntry.CONTENT_TYPE",WeatherContract.WeatherEntry.CONTENT_TYPE,type);

        String testLocation="94074";
        type=mContext.getContentResolver().getType(WeatherContract.WeatherEntry.buildWeatherLocation(testLocation));
        assertEquals("Error:WeatherEntry CONTENT_URI with Location should return WeatherEntry.CONTENT_TYPE",WeatherContract.WeatherEntry.CONTENT_TYPE,type);

        long test_Date=1419120000L;
        type=mContext.getContentResolver().getType(WeatherContract.WeatherEntry.buildWeatherLocationwithdate(testLocation,test_Date));
        assertEquals("Error:WeatherEntry CONTENT_URI with Location  and date should return WeatherEntry.CONTENT_ITEM_TYPE",WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE,type);

        type=mContext.getContentResolver().getType(WeatherContract.LocationEntry.CONTENT_URI);
        assertEquals("Error:LocationEntry CONTENT_URI  should return LocationEntry.CONTENT_TYPE",WeatherContract.LocationEntry.CONTENT_TYPE,type);

    }

    public void testBasicWeatherQuery(){

        WeatherDBHelper mydb=new WeatherDBHelper(mContext);
        SQLiteDatabase db=mydb.getWritableDatabase();

        ContentValues testValues=TestUtilities.createNorthPoleLocationValues();
        long locationRowId=TestUtilities.insertNorthPoleLocationValues(mContext);

        assertTrue(locationRowId!=-1);

        ContentValues weatherValues=TestUtilities.createWeatherValues(locationRowId);
        long weatherId=db.insert(WeatherContract.WeatherEntry.TABLE_NAME,null,weatherValues);

        assertTrue(weatherId != -1);

        db.close();

        Cursor weatherCursor=mContext.getContentResolver().query(WeatherContract.WeatherEntry.CONTENT_URI,null,null,null,null);
        TestUtilities.ValidateCursor("Test weather cursor", weatherCursor, weatherValues);

    }

//    public void testBasicLocationQuery(){
//
//        WeatherDBHelper mydb=new WeatherDBHelper(mContext);
//        SQLiteDatabase db=mydb.getWritableDatabase();
//
//        ContentValues testValues=TestUtilities.createNorthPoleLocationValues();
//        long locationRowId=db.insert(WeatherContract.LocationEntry.TABLE_NAME,null,testValues);
//
//        assertTrue(locationRowId != -1);
//
//        Cursor locationCursor=mContext.getContentResolver().query(WeatherContract.LocationEntry.CONTENT_URI,null,null,null,null);
//        TestUtilities.ValidateCursor("test Location queries.",locationCursor,testValues);
//
//        if(Build.VERSION.SDK_INT>=19){
//            assertEquals(locationCursor.getNotificationUri(),WeatherContract.LocationEntry.CONTENT_URI);
//        }
//    }
}
