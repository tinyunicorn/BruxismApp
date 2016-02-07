package com.example.emma.bruxismapp;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.util.Calendar;


/**
 * Created by Emma on 27/08/2015.
 */
public class MyService extends Service {

    private static final String TAG = "MyService";

    private boolean isRunning  = false;



    SQLiteDatabase myDB = null;

    String TableName = "myTable";

    String Data=null;

    String startTime = null;
    String dateInfo = null;
    String nextLine = "\n";




    public static final String BROADCAST_ACTION = "com.example.emma.myService.BROADCAST";
    public static final String SERVICE_SWITCHOFF = "com.example.emma.myService.SWITCHOFF";
    public static final String BRUXISM_END = "com.example.emma.myService.BRUXISM_END";

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {

                dataReader();

                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }



    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        announceEnd();

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void dataReader(){

        try {
            //read input stream
            InputStream is = getAssets().open("bruxismdata1.csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            //compare value to the previous one
            int line = Integer.parseInt(in.readLine());
            int line1 = Integer.parseInt(in.readLine());

            while (line1 != 0) {
                if(line - line1 > 8000000){
                    Log.i(TAG, "Bruxism event!");
                    startTime = getTime();
                    dateInfo = getDate();
                    sentMessage(startTime);
                    long startTime1 = SystemClock.elapsedRealtimeNanos();
                    FileOutputStream fo = openFileOutput(dateInfo+" - "+startTime + ".csv", MODE_PRIVATE);
                    while((line1 - line) < 8000000){
                        Log.i(TAG, "Dit gebeurt wel");
                        dataWriter(line1, fo);
                        line = line1;
                        line1 = Integer.parseInt(in.readLine());
                    }
                    long endTime = SystemClock.elapsedRealtimeNanos();
                    long elapsedTime = endTime - startTime1;
                    endTimeWriter(elapsedTime, fo);
                    bruxismEnd();
                    fo.close();
                }
                line = line1;
                line1 = Integer.parseInt(in.readLine());
            }
        } catch(Exception e) { e.printStackTrace(); }


    }

    public void dataWriter(int line1, FileOutputStream fo){
        try {
            //opens a new file named after the time bruxism happened

            //writes the one input, the next one will pass through in the next iteration.
            String line = String.valueOf(line1);
            fo.write(line.getBytes());
            fo.write(nextLine.getBytes());
            Log.i(TAG, "I get to this point!");
        }catch(Exception e) {e.printStackTrace(); }
    }

    public void sentMessage(String time){
        //Creates a new intent containing an URI object, BROADCAST_ACTION is a custom Intent action
        Intent localIntent = new Intent(BROADCAST_ACTION).putExtra("EXTRA_TIME",time);
        //Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    public void bruxismEnd() {
        Intent bEndIntent = new Intent(BRUXISM_END);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(bEndIntent);
    }

    public void announceEnd(){
        Intent endIntent = new Intent(SERVICE_SWITCHOFF);
                //Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(endIntent);
        System.out.println("De eindintent wordt verstuurd!");
    }

    public String getTime(){
        Calendar c = Calendar.getInstance();
        //int miliSec = c.get(Calendar.MILLISECOND);
        int sec = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);



        String time = String.valueOf(hour)+":"+String.valueOf(minute)+":"+String.valueOf(sec);
        return time;
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
        return date;
    }

    public void endTimeWriter(long time, OutputStream fo){
        try {

            String time1 = String.valueOf(time / 1000000000);

            //Calendar c = Calendar.getInstance();
            //String date = c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR)+"\n";

            fo.write(("Date: " + getDate() + "\n").getBytes());
            fo.write(("Time: " + startTime + "\n").getBytes());
            fo.write(("Duration in seconds: " + time1).getBytes());
        }catch(Exception e){e.printStackTrace(); }
    }
}
