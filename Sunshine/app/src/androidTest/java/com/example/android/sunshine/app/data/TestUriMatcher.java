package com.example.android.sunshine.app.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by chhavi on 9/7/16.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final String LOCATION_QUERY = "London";
    private static final long TEST_DATE = 1419033600L;
    private static final long TEST_LOCATION_ID = 10L;

    private static final Uri TEST_WEATHER_DIR = WeatherContract.WeatherEntry.CONTENT_URI;
    private static final Uri TEST_WEATHER_WITH_LOCATION_DIR = WeatherContract.WeatherEntry.buildWeatherLocation(LOCATION_QUERY);
    private static final Uri TEST_WEATHER_WITH_LOCATION_AND_DATE_DIR = WeatherContract.WeatherEntry.buildWeatherLocationwithdate(LOCATION_QUERY,TEST_DATE);
    private static final Uri TEST_LOCATION_DIR = WeatherContract.LocationEntry.CONTENT_URI;

    public void testUriMatcher(){

        UriMatcher testMatcher = WeatherProvider.buildUriMatcher();

        assertEquals("Error:Weather URI matched incorrectly",testMatcher.match(TEST_WEATHER_DIR),WeatherProvider.WEATHER);
        assertEquals("Error:Weather with location URI matched incorrectly",testMatcher.match(TEST_WEATHER_WITH_LOCATION_DIR),WeatherProvider.WEATHER_WITH_LOCATION);
        assertEquals("Error:Weather with location and date URI matched incorrectly",testMatcher.match(TEST_WEATHER_WITH_LOCATION_AND_DATE_DIR),WeatherProvider.WEATHER_WITH_LOCATION_AND_DATE);
        assertEquals("Error:Location URI matched incorrectly",testMatcher.match(TEST_LOCATION_DIR),WeatherProvider.LOCATION);


    }



}