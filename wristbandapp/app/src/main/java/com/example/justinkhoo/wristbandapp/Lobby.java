package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
//import android.telephony.gsm.SmsManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class Lobby extends Activity implements MyAsyncResponse {

    ListView listView;
    String[] items = { "Step Counter", "Heart Rate", "Contact Temperature"};

    Button syncButton;

    MyLocationManager mylocationmanager;

    DBTools dbtools = new DBTools(this);
    String lat = "";
    String longi = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mylocationmanager = new MyLocationManager(Lobby.this);

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

    public void goToContact(View view){
//        Intent intent = new Intent(Lobby.this, EmergencyContact.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

//        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//        smsIntent.putExtra("address", "3330349");
//        smsIntent.setType("vnd.android-dir/mms-sms");
//        sendSms("9092347237", "android sms_send testing", false);
//        sendSms("9092347237", "#wakeup", false);
//        sendSms("9092347237", "muhahahahaha", false);
//        sendSms("6268272363", "wake up!!!", false);
//        sendSms("6268272363", "muhahahahaha", false);
       // startActivity(smsIntent);

        Log.d(mylocationmanager.getLatitude(), "lati : ");
        Log.d(mylocationmanager.getLongitude(), "long : ");

//        callService mycallService = new callService(Lobby.this);
//        mycallService.printCoor();
    }
    private void sendSms(String phonenumber,String message, boolean isBinary)
    {
        SmsManager manager = SmsManager.getDefault();
        Intent notificationIntent = new Intent(this, Lobby.class);
        PendingIntent piSend = PendingIntent.getBroadcast(this, 0, notificationIntent, 0); //new Intent(SMS_SENT)
        PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, notificationIntent, 0); //new Intent(SMS_DELIVERED)

        if(isBinary)
        {
            byte[] data = new byte[message.length()];

            for(int index=0; index<message.length() && index < 160; ++index)
            {
                data[index] = (byte)message.charAt(index);
            }
            manager.sendDataMessage(phonenumber, null, (short) 121, data,piSend, piDelivered);
        }
        else
        {
            int length = message.length();

            if(length > 160)
            {
                ArrayList<String> messagelist = manager.divideMessage(message);

                manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
            }
            else
            {
                manager.sendTextMessage(phonenumber, null, message, piSend, piDelivered);
            }
        }
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
