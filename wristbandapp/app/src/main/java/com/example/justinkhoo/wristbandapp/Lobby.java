package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class Lobby extends Activity implements MyAsyncResponse {

    ListView listView;
    String[] items = { "Step Counter", "Heart Rate", "Contact Temperature"};

    Button syncButton;

    DBTools dbtools = new DBTools(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        syncButton = (Button) findViewById( R.id.syncButton );
        syncButton.setTypeface(font);

        listView = (ListView) findViewById(R.id.lobbyListView);

        ArrayList<String> lobbyItems = new ArrayList<String>();

        for(int i = 0; i < items.length; i++) {
            lobbyItems.add(items[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_lobby_row, lobbyItems);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        Intent intent1 = new Intent(Lobby.this, Steps.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Lobby.this, Heartrate.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                        break;
                    case 2:
                        Intent intent3 = new Intent(Lobby.this, Temperatures.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void syncData(View view) {
        ArrayList<HashMap<String, String>> data;

        data = dbtools.getTemperature();
        data = dbtools.getSteps();
        data = dbtools.getHeartbeat();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lobby, menu);
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

    @Override
    public void processFinish(String output) {

    }
}
