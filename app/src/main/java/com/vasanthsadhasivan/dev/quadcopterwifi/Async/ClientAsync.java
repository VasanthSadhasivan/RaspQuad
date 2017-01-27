package com.vasanthsadhasivan.dev.quadcopterwifi.Async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vasanthsadhasivan.dev.quadcopterwifi.MainActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Admin on 12/28/2016.
 */

public class ClientAsync extends AsyncTask<String, String, Void> {

    public static String TAG = "ClientAsync";
    public static String serverName;
    public static int port = 90;
    public static Socket client;
    public static PrintWriter out;
    public static BufferedReader in;
    public static String data;
    public static boolean done=false;



    @Override
    protected Void doInBackground(String... key) {

        data = "";
        String sendData = "";
        try {
            if (!client.isConnected() && !done) {
                client = new Socket(serverName, port);
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                Log.v(TAG, "Connected to: " + serverName + " : " + port);
            }
            if (!client.isClosed() && key.length == 1) {
                data = key[0] + "," + (MainActivity.switchValues.get(key[0]));
                sendData += rightPad(data, 20, '*');

                out.println(sendData);
                Log.v(TAG, sendData);
                //InputStream inFromServer = client.getInputStream();
                //DataInputStream in = new DataInputStream(inFromServer);
                //this.publishProgress("Server says: " + in.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }

        return str.concat(padding(pads, padChar));
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }

    public static void endConnection() {
        try {
            done=true;
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //((TextView) MainActivity.activity.findViewById(R.id.monitor)).setText(values[0]+"\n");
    }

}
