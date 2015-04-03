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
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by harrison on 4/2/15.
 */
public class StepHandlerThread extends Thread {

    SharedPreferences sharedPreferences;
    Handler handler;
    Context context;

    public StepHandlerThread(Handler handler2, Context context2) {
        context = context2;
        handler = handler2;
        sharedPreferences = context.getSharedPreferences("com.example.justinkhoo.wristbandapp", Context.MODE_PRIVATE);
    }

    public void run() {
        while (true) {
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            int stepCount = Integer.parseInt(sharedPreferences.getString("stepCount", "1"));

            b.putString("message", String.valueOf(stepCount));
            msgObj.setData(b);

            handler.sendMessage(msgObj);

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
}
