package com.example.emma.bruxismapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import android.graphics.Color;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //trying a toggle button as it would be nicer than two seperate buttttonsnsss
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    TextView onOff = (TextView) findViewById(R.id.onoff);
                    onOff.setText(R.string.detectionon);
                    startService(intent);
                } else {
                    // The toggle is disabled
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    stopService(intent);
                }
            }
        });

//        //starting service
//        findViewById(R.id.start_service).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MyService.class);
//                startService(intent);
//            }
//        });
//
//
//        //service onDestroy callback method will be called
//        findViewById(R.id.stop_service).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MyService.class);
//                stopService(intent);
//
//
//            }
//        });



        LocalBroadcastManager myS = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.BROADCAST_ACTION);
        intentFilter.addAction(MyService.SERVICE_SWITCHOFF);
        intentFilter.addAction(MyService.BRUXISM_END);
        myS.registerReceiver(sReceiver, intentFilter);


    }

    private BroadcastReceiver sReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MyService.BROADCAST_ACTION)){
                //bruxism has started
                ImageView alarm = (ImageView) findViewById(R.id.imageView);
                alarm.setImageResource(R.drawable.googleplaystorerb);
                //alarm.setText(intent.getExtras().toString());
                System.out.println("It worked!");
            }

            if(intent.getAction().equals(MyService.BRUXISM_END)){
                ImageView alarm = (ImageView) findViewById(R.id.imageView);
                alarm.setImageResource(R.drawable.googleplaystorebb);
                System.out.println("Het gaat te snel om te zien!");
            }

            if(intent.getAction().equals(MyService.SERVICE_SWITCHOFF)){
                //the detection input has stopped
                ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
                toggle.toggle();TextView onOff = (TextView) findViewById(R.id.onoff);
                onOff.setText(R.string.detectionoff);
                System.out.println("I get the switchoff message!");
            }


        }
    };

    public void startList(View view)
    {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


