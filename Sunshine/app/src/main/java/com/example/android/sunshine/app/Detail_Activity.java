package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Detail_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.weather_detail_container,new Detail_ActivityFragment()).commit();
        }


    }

}