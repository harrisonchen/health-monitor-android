package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class DBTest extends Activity {

    DBTools dbtools = new DBTools(this);
    ListView listview;
    ArrayAdapter<String> deviceArrayAdapter;
    ArrayList<String> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        HashMap<String, String> motion = new HashMap<String, String>();
        HashMap<String, String> steps = new HashMap<String, String>();
        HashMap<String, String> heartbeat = new HashMap<String, String>();
        HashMap<String, String> temperature = new HashMap<String, String>();

        motion.put("x", "10.1");
        motion.put("y", "20.2");
        motion.put("z", "30.3");

        steps.put("step_count", "4000");

        heartbeat.put("beats_per_minute", "50");

        temperature.put("fahrenheit", "60");

        dbtools.addMotion(motion);
        dbtools.addSteps(steps);
        dbtools.addHeartbeat(heartbeat);
        dbtools.addTemperature(temperature);

        listview = (ListView) findViewById(R.id.listView);
        ArrayList<String> items = new ArrayList<String>();

        items.add(dbtools.getMotion().get(0).get("y"));
        items.add(dbtools.getSteps().get(0).get("step_count"));
        items.add(dbtools.getHeartbeat().get(0).get("beats_per_minute"));
        items.add(dbtools.getTemperature().get(0).get("fahrenheit"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_lobby_row, items);
        listview.setAdapter(adapter);
    }

    public void addToDB(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dbtest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
