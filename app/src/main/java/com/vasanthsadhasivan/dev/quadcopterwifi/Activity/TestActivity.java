package com.vasanthsadhasivan.dev.quadcopterwifi.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vasanthsadhasivan.dev.quadcopterwifi.R;
import com.vasanthsadhasivan.dev.quadcopterwifi.Views.JoystickView;

/**
 * Created by Admin on 1/25/2017.
 */

public class TestActivity extends AppCompatActivity{

    JoystickView joystickView1, joystickView2;
    int previousValueX1;
    int previousValueX2;
    int previousValueY1;
    int previousValueY2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

    }


}
