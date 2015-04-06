package com.example.justinkhoo.wristbandapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.location.LocationListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harrison on 4/3/15.
 */
public class MonitorThread extends Thread {

    SharedPreferences sharedPreferences;
    Context context;
    DBTools dbtools;
    MyLocationManager mylocationmanager;
    String lat;
    String longi;

    public MonitorThread(Context context2) {
        context = context2;
        dbtools = new DBTools(context);
        mylocationmanager = new MyLocationManager(context);
        lat = mylocationmanager.getLatitude();
        longi = mylocationmanager.getLongitude();
        sharedPreferences = context.getSharedPreferences("com.example.justinkhoo.wristbandapp", Context.MODE_PRIVATE);
    }

    public void run() {
        while (true) {

            lat = mylocationmanager.getLatitude();
            longi = mylocationmanager.getLongitude();

            emergency();

            int stepCount = Integer.parseInt(sharedPreferences.getString("stepCount", "0"));
            int lastStepCount = Integer.parseInt(sharedPreferences.getString("lastStepCount", "0"));
            int lastReceivedStepCount = Integer.parseInt(sharedPreferences.getString("lastReceivedStepCount", "0"));
            int notMoving = Integer.parseInt(sharedPreferences.getString("notMoving", "0"));

            // MOCK DATA
            sharedPreferences.edit().putString("lastReceivedStepCount", String.valueOf(lastReceivedStepCount + 1)).apply();
            sharedPreferences.edit().putString("fahrenheit", String.valueOf(lastReceivedStepCount + 1)).apply();
            sharedPreferences.edit().putString("beats_per_minute", String.valueOf(lastReceivedStepCount + 1)).apply();
            sharedPreferences.edit().putString("notMoving", String.valueOf(notMoving + 1)).apply();
            // ---------

//            if(lastStepCount > lastReceivedStepCount) {
//                if(stepCount == lastStepCount + lastReceivedStepCount) {
//                    sharedPreferences.edit().putString("notMoving", String.valueOf(notMoving + 1)).apply();
//                } else {
//                    sharedPreferences.edit().putString("notMoving", "0").apply();
//                    stepCount = lastStepCount + lastReceivedStepCount;
//                }
//            }
//            else {
//                if(stepCount == lastReceivedStepCount - lastStepCount) {
//                    sharedPreferences.edit().putString("notMoving", String.valueOf(notMoving + 1)).apply();
//                } else {
//                    sharedPreferences.edit().putString("notMoving", "0").apply();
//                    stepCount = lastReceivedStepCount - lastStepCount;
//                }
//            }

            notMoving = Integer.parseInt(sharedPreferences.getString("notMoving", "0"));

            // Mocking emergency!
            if(notMoving >= 50) {
                sharedPreferences.edit().putString("emergencyNow", "1").apply();
            }
            // ------------------

            if(notMoving == 40 && Integer.parseInt(sharedPreferences.getString("beats_per_minute", "0")) > 100
                    && sharedPreferences.getString("fastHeartNotification", "0").equals("0")) {
                sharedPreferences.edit().putString("fastHeartNotification", "1").apply();
            }

            if(notMoving == 50) {
                sharedPreferences.edit().putString("notMoving", "0").apply();
                sharedPreferences.edit().putString("moveNotification", "1").apply();
            }

            if(sharedPreferences.getString("moveNotification", "0").equals("1")) {
                sharedPreferences.edit().putString("moveNotification", "0").apply();
                buildNotification("OneBand", "You havn't moved in a while. Get up!", Steps.class, 1);
            }
            if(sharedPreferences.getString("fastHeartNotification", "0").equals("1")) {
                buildNotification("OneBand", "You're heart is at a constant high. Maybe you should check a doctor?", Heartrate.class, 3);
                if(notMoving <= 50) {
                    sharedPreferences.edit().putString("fastHeartNotification", "0").apply();
                }
            }

            Log.d("notMoving", sharedPreferences.getString("notMoving", "0"));
            Log.d("stepCount", String.valueOf(stepCount));
            Log.d("lastReceivedStepCount", String.valueOf(sharedPreferences.getString("lastReceivedStepCount", "0")));

            sharedPreferences.edit().putString("stepCount", String.valueOf(stepCount)).apply();

            if(stepCount >= Double.parseDouble(sharedPreferences.getString("stepGoal", "999999"))) {
                if(sharedPreferences.getString("stepsAchieved", "0").equals("0")) {
                    sharedPreferences.edit().putString("stepsAchieved", "1").apply();
                    buildNotification("OneBand", "You have reached your step goal!", Steps.class, 2);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void emergency() {
        if(sharedPreferences.getString("emergencyNow", "0").equals("1")) {
            //contacting emergency contacts
            Log.d( lat, "latitude : ");
            Log.d( longi, "Longitude : ");
            ArrayList<HashMap<String, String>> contacts = dbtools.getEmergencyContacts();
            for(int i = 0; i < contacts.size(); i++) {
                HashMap<String, String> contact = contacts.get(i);
//                sendSms(contact.get("phone"), "I'm in trouble. I'm at location http://maps.google.com/?q="+lat+","+longi, false);
            }

            Log.d("", "Sending SMS to emergency contacts!");
            sharedPreferences.edit().putString("emergencyNow", "0").apply();
        }
    }

    private void sendSms(String phonenumber,String message, boolean isBinary)
    {
        SmsManager manager = SmsManager.getDefault();
        Intent notificationIntent = new Intent(context, Lobby.class);
        PendingIntent piSend = PendingIntent.getBroadcast(context, 0, notificationIntent, 0); //new Intent(SMS_SENT)
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, notificationIntent, 0); //new Intent(SMS_DELIVERED)

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

    public void cancel() {
        interrupt();
    }

    public void buildNotification(String title, String content, Class classname, Integer id) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.oneband_icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setDefaults(Notification.DEFAULT_VIBRATE);

        Intent notificationIntent = new Intent(context, classname);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
    }
}
