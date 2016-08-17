package com.example.android.sunshine.app.data;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by chhavi on 5/7/16.
 */
public class FullTestSuite extends TestSuite{

    public static Test Suite(){
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();

    }

    public FullTestSuite(){super();}

}
