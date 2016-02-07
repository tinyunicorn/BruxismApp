package com.example.emma.bruxismapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Emma on 06/09/2015.
 */
public class ListActivity extends AppCompatActivity {

    public final static String FILE_NAME = "com.example.emma.bruxismapp.FILENAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String[] fileArray = fileList();
        String[] listArray = new String[fileList().length];


        for(int i = fileList().length-1; i >= 0; i--){

                    listArray[fileList().length - i - 1] = fileArray[i].substring(0, fileArray[i].indexOf("."));

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item_data,
                R.id.list_item, listArray);

        //Get a reference to the ListView, and attach the adapter to it.
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //show a little message
                String text = ((TextView)view.findViewById(R.id.list_item)).getText().toString()+".csv";
                //Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                //toast.show();
                Intent intent = new Intent(ListActivity.this, ItemViewerActivity.class).putExtra(FILE_NAME, text);
                startActivity(intent);
            }
        });
        

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listactivity, menu);
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
