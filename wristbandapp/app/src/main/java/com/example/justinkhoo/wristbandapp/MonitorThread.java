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
import android.util.Log;

/**
 * Created by harrison on 4/3/15.
 */
public class MonitorThread extends Thread {

    SharedPreferences sharedPreferences;
    Context context;

    public MonitorThread(Context context2) {
        context = context2;
        sharedPreferences = context.getSharedPreferences("com.example.justinkhoo.wristbandapp", Context.MODE_PRIVATE);
    }

    public void run() {
        while (true) {
            int stepCount = Integer.parseInt(sharedPreferences.getString("stepCount", "0"));
            int lastStepCount = Integer.parseInt(sharedPreferences.getString("lastStepCount", "0"));
            int lastReceivedStepCount = Integer.parseInt(sharedPreferences.getString("lastReceivedStepCount", "0"));
            int notMoving = Integer.parseInt(sharedPreferences.getString("notMoving", "0"));

//            sharedPreferences.edit().putString("lastReceivedStepCount", String.valueOf(lastReceivedStepCount + 1)).apply();

            if(lastStepCount > lastReceivedStepCount) {
                if(stepCount == lastStepCount + lastReceivedStepCount) {
                    sharedPreferences.edit().putString("notMoving", String.valueOf(notMoving + 1)).apply();
                } else {
                    sharedPreferences.edit().putString("notMoving", "0").apply();
                    stepCount = lastStepCount + lastReceivedStepCount;
                }
            }
            else {
                if(stepCount == lastReceivedStepCount - lastStepCount) {
                    sharedPreferences.edit().putString("notMoving", String.valueOf(notMoving + 1)).apply();
                } else {
                    sharedPreferences.edit().putString("notMoving", "0").apply();
                    stepCount = lastReceivedStepCount - lastStepCount;
                }
            }

            notMoving = Integer.parseInt(sharedPreferences.getString("notMoving", "0"));

            if(notMoving >= 200) {
                sharedPreferences.edit().putString("notMoving", "0").apply();
                buildNotification("OneBand", "You havn't moved in a while. Get up!", Steps.class, 1);
            }

            Log.d("notMoving", sharedPreferences.getString("notMoving", "0"));
            Log.d("stepCount", String.valueOf(stepCount));
            Log.d("lastReceivedStepCount", String.valueOf(sharedPreferences.getString("lastReceivedStepCount", "0")));

            sharedPreferences.edit().putString("stepCount", String.valueOf(stepCount)).apply();

            if(stepCount >= Double.parseDouble(sharedPreferences.getString("stepGoal", "999999"))) {
                if(sharedPreferences.getString("stepsAchieved", "0").equals("0")) {
                    sharedPreferences.edit().putString("stepsAchieved", "1").apply();
                    buildNotification("OneBand", "You have reached your step goal!", Steps.class, 1);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        interrupt();
    }

    public void buildNotification(String title, String content, Class classname, Integer id) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
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
