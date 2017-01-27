package com.vasanthsadhasivan.dev.quadcopterwifi;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vasanthsadhasivan.dev.quadcopterwifi.Async.ClientAsync;
import com.vasanthsadhasivan.dev.quadcopterwifi.Views.JoystickView;

import java.net.Socket;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    int previousValueX1;
    int previousValueX2;
    int previousValueY1;
    int previousValueY2;
    JoystickView joystickView1, joystickView2;
    public String TAG = "MainActivity";
    public static HashMap seekBars = new HashMap();
    public static HashMap switchValues = new HashMap();
    public static Activity activity;
    EditText ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joystickView1 = (JoystickView) findViewById(R.id.joystick1);
        joystickView2 = (JoystickView) findViewById(R.id.joystick2);
        joystickView1.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                angle = ((-1*angle)+90);
                if(angle < 0){
                    angle = 360+angle;
                }
                int x = (int)((((int)((Math.cos(Math.toRadians(angle))*power)/2))*2+100)*0.9);
                int y = (int)((((int)((Math.sin(Math.toRadians(angle))*power)/2))*2+100)*0.9);
                if(previousValueX1 == x && previousValueY1 == y){
                    return;
                }
                if(previousValueX1 != x){
                    switchValues.put("yaw", x);
                    String[] data = {"yaw"};
                    (new ClientAsync()).execute(data);
                }
                if(previousValueY1 != y){
                    switchValues.put("throttle", y);
                    String[] data = {"throttle"};
                    (new ClientAsync()).execute(data);
                }
                previousValueX1 = x;
                previousValueY1 = y;
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
        joystickView2.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                angle = ((-1*angle)+90);
                if(angle < 0){
                    angle = 360+angle;
                }
                int x = (int)((((int)Math.cos(Math.toRadians(angle))*power)+100)*0.9);
                int y = (int)((((int)Math.sin(Math.toRadians(angle))*power)+100)*0.9);
                if(previousValueX2 == x && previousValueY2 == y){
                    return;
                }
                if(previousValueX2 != x){
                    switchValues.put("roll", x);
                    String[] data = {"roll"};
                    (new ClientAsync()).execute(data);
                }
                if(previousValueY2 != y){
                    switchValues.put("pitch", y);
                    String[] data = {"pitch"};
                    (new ClientAsync()).execute(data);
                }
                previousValueX2 = x;
                previousValueY2 = y;
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        ClientAsync.client = new Socket();
        seekBars.put("throttle", (SeekBar) findViewById(R.id.throttle));
        seekBars.put("yaw", (SeekBar) findViewById(R.id.yaw));
        seekBars.put("pitch", (SeekBar) findViewById(R.id.pitch));
        seekBars.put("roll", (SeekBar) findViewById(R.id.roll));
        seekBars.put("flmswitch", (SeekBar) findViewById(R.id.flmswitch));
        seekBars.put("aux", (SeekBar) findViewById(R.id.aux));

        for (Object key : seekBars.keySet()) {
            ((SeekBar)seekBars.get((String) key)).setOnSeekBarChangeListener(this);
        }

        switchValues.put("throttle", 0);
        switchValues.put("yaw", 0);
        switchValues.put("pitch", 0);
        switchValues.put("roll", 0);
        switchValues.put("flmswitch", 0);
        switchValues.put("aux", 0);

        ipAddress = (EditText) findViewById(R.id.ipaddress);

        activity = this;
    }

    public void onOkClicked(View view) {
        ClientAsync.serverName = ((EditText)findViewById(R.id.ipaddress)).getText().toString();
        (new ClientAsync()).execute(new String[0]);
        ((Button)view).setText("Connected");
    }


    public void onEndClicked(View view) {
        ClientAsync.endConnection();
        ((TextView)view).setText("Ended");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.v(TAG, "Progress bar: "+getResources().getResourceEntryName(seekBar.getId())+": "+i);
        switchValues.put(getResources().getResourceEntryName(seekBar.getId()), i);
        String[] data = {getResources().getResourceEntryName(seekBar.getId())};
        (new ClientAsync()).execute(data);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onPowerOffClicked(View view) {
        String[] data = {"poweroff"};
        (new ClientAsync()).execute(data);
    }


}
