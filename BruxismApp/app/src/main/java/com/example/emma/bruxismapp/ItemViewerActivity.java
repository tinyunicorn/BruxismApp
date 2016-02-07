package com.example.emma.bruxismapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ItemViewerActivity extends AppCompatActivity {

    private String finito = null;
    private String dataString = null;
    private String infoString = null;
    private String finalString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);
        Intent intent = getIntent();


        String content = (intent.getStringExtra(ListActivity.FILE_NAME));


        TextView name = (TextView) findViewById(R.id.filename);


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(openFileInput(content)));
            finito= "";
            String line = in.readLine();
            while(line !=  null){
                finito = finito +"\n"+line;

                line = in.readLine();
            }

            int index = finito.indexOf("Date");
            dataString = finito.substring(0, index);
            infoString = finito.substring(index);

            finalString = infoString +"\n\nMeasured values:"+ dataString;

            name.setText(finalString);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_viewer, menu);
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
