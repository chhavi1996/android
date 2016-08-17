package com.example.android.sunshine.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by chhavi on 9/7/16.
 */
public class WeatherProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher=buildUriMatcher();
    private WeatherDBHelper mOpenHelper;

    static final int WEATHER=100;
    static final int WEATHER_WITH_LOCATION=101;
    static final int WEATHER_WITH_LOCATION_AND_DATE=102;
    static final int LOCATION=300;

    private static final SQLiteQueryBuilder sWeatherbyLocationSettingQueryBuilder;

    static {
        sWeatherbyLocationSettingQueryBuilder=new SQLiteQueryBuilder();

        //weather INNER join location ON weather.location_id=location._id
        sWeatherbyLocationSettingQueryBuilder.setTables(WeatherContract.WeatherEntry.TABLE_NAME+" INNER JOIN "
                +WeatherContract.LocationEntry.TABLE_NAME+" ON "+WeatherContract.WeatherEntry.TABLE_NAME+"."+
                                    WeatherContract.WeatherEntry.COLUMN_LOC_KEY+"="
                +WeatherContract.LocationEntry.TABLE_NAME+"."+WeatherContract.LocationEntry._ID);
    }

    private static final String LocationSettingSelection=WeatherContract.LocationEntry.TABLE_NAME+"."+
                                    WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING+"=?";

    private static final String LocationSettingWithStartdateSelection=WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING+"=? AND "+
            WeatherContract.WeatherEntry.COLUMN_DATE+">=?";

    private static final String LocationSettingAndDaySelection=WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING+"=? AND "+
            WeatherContract.WeatherEntry.COLUMN_DATE+"=?";

    private Cursor getWeatherByLocationSetting(Uri uri,String[] projection,String sortOrder){
        String locationSetting=WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
        long StartDate=WeatherContract.WeatherEntry.getStartDateFromUri(uri);

        String[] SelectionArgs;
        String selection;

        if(StartDate==0){
            selection=LocationSettingSelection;
            SelectionArgs=new String[]{locationSetting};

        }
        else{
            selection=LocationSettingWithStartdateSelection;
            SelectionArgs=new String[]{locationSetting,Long.toString(StartDate)};
        }

        return sWeatherbyLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),projection,selection,SelectionArgs,null,null,sortOrder);
    }

    private Cursor getWeatherByLocationSettingandDate(Uri uri,String[] projection,String sortOrder){
        String locationSetting=WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
        long date=WeatherContract.WeatherEntry.getDateFromUri(uri);

        String[] selectionArgs=new String[]{locationSetting,Long.toString(date)};
        String selection=LocationSettingAndDaySelection;

        return sWeatherbyLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);

    }



    static UriMatcher buildUriMatcher(){

        UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        String authority=WeatherContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,WeatherContract.PATH_WEATHER,WEATHER);
        matcher.addURI(authority,WeatherContract.PATH_WEATHER+"/*",WEATHER_WITH_LOCATION);
        matcher.addURI(authority,WeatherContract.PATH_WEATHER+"/*/#",WEATHER_WITH_LOCATION_AND_DATE);
        matcher.addURI(authority,WeatherContract.PATH_LOCATION,LOCATION);

        return matcher;


    }

    @Override
    public boolean onCreate() {
        mOpenHelper=new WeatherDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)){

            case WEATHER_WITH_LOCATION_AND_DATE:
                retCursor=getWeatherByLocationSettingandDate(uri,projection,sortOrder);
                break;
            case WEATHER_WITH_LOCATION:
                retCursor=getWeatherByLocationSetting(uri,projection,sortOrder);
                break;
            case WEATHER:
                retCursor=mOpenHelper.getReadableDatabase().query(WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case LOCATION:
                retCursor=mOpenHelper.getReadableDatabase().query(WeatherContract.LocationEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);;
                break;

            default:throw new UnsupportedOperationException("Unknown Uri "+uri);

        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match= sUriMatcher.match(uri);

        switch (match){

            case WEATHER:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case LOCATION:
                return WeatherContract.LocationEntry.CONTENT_TYPE;
            case WEATHER_WITH_LOCATION:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case WEATHER_WITH_LOCATION_AND_DATE:
                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+uri);

        }

    }

    private void normalizeDate(ContentValues values){

        if(values.containsKey(WeatherContract.WeatherEntry.COLUMN_DATE)){
            long date=values.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
            values.put(WeatherContract.WeatherEntry.COLUMN_DATE,WeatherContract.normalizeDate(date));
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        Uri retUri;

        switch (match){
            case WEATHER:{
                normalizeDate(values);
                long _id=db.insert(WeatherContract.WeatherEntry.TABLE_NAME,null,values);
                if(_id>0){

                    retUri=WeatherContract.WeatherEntry.buildWeatherUri(_id);
                }
                else
                    throw new android.database.SQLException("Failed to insert a row at "+uri);
                break;
            }

            case LOCATION:
            {
                long _id=db.insert(WeatherContract.LocationEntry.TABLE_NAME,null,values);
                if(_id>0){

                    retUri=WeatherContract.LocationEntry.buildLocationUri(_id);
                }
                else
                    throw new android.database.SQLException("Failed to insert a row at "+uri);
                break;

            }

            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);


        }

        getContext().getContentResolver().notifyChange(uri,null);

        return retUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);

        switch (match){

            case WEATHER:
                db.beginTransaction();
                int retCount=0;

                try {
                    for (ContentValues value : values) {

                        normalizeDate(value);
                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            retCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri,null);
                return retCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsDeleted=0;

        if(selection==null)
            selection="1";

        switch (match){
            case WEATHER:
                rowsDeleted=db.delete(WeatherContract.WeatherEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case LOCATION:
                rowsDeleted=db.delete(WeatherContract.LocationEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri :"+uri);

        }

        if(rowsDeleted!=0){

            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsUpdated=0;

        if(selection==null)
            selection="1";

        switch (match){
            case WEATHER:
                rowsUpdated=db.update(WeatherContract.WeatherEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LOCATION:
                rowsUpdated=db.update(WeatherContract.LocationEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri :"+uri);

        }

        if(rowsUpdated!=0){

            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }
}
