package com.example.justinkhoo.wristbandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by harrison on 4/5/15.
 */
public class TemperatureHandlerThread extends Thread {
    SharedPreferences sharedPreferences;
    Handler handler;
    Context context;

    public TemperatureHandlerThread(Handler handler2, Context context2) {
        context = context2;
        handler = handler2;
        sharedPreferences = context.getSharedPreferences("com.example.justinkhoo.wristbandapp", Context.MODE_PRIVATE);
    }

    public void run() {
        while (true) {
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            int fahrenheit = Integer.parseInt(sharedPreferences.getString("fahrenheit", "83"));

            b.putString("message", String.valueOf(fahrenheit));
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
