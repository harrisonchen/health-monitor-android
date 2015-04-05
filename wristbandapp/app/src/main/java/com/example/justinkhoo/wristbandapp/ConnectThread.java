package com.example.justinkhoo.wristbandapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ConnectThread extends Thread implements MyAsyncResponse {
    private BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final BluetoothAdapter bluetoothAdapter;
    private final Handler mHandler;
    DBTools dbtools;
    SharedPreferences sharedPreferences;

    public ConnectedThread connectedThread;

    private UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter, Handler handler, Context context) {

        sharedPreferences = context.getSharedPreferences("com.example.justinkhoo.wristbandapp", Context.MODE_PRIVATE);

        BluetoothSocket tmp = null;
        bluetoothDevice = device;
        bluetoothAdapter = adapter;
        mHandler = handler;
        dbtools = new DBTools(context);

        try {
            tmp = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d("STEP 1 FAILED", "");
        }
        bluetoothSocket = tmp;
    }

    public void run() {
        bluetoothAdapter.cancelDiscovery();

        try {
            bluetoothSocket.connect();
            BluetoothSocketListener bsl = new BluetoothSocketListener(bluetoothSocket);
            Thread messageListener = new Thread(bsl);
            messageListener.start();
//            connectedThread = new ConnectedThread(bluetoothSocket);
//            connectedThread.run();
        }
        catch (IOException e) {
            e.printStackTrace();

            Log.d("Fail Fail Fail:,", "Failed");

            try {
                bluetoothSocket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
                bluetoothSocket.connect();

                BluetoothSocketListener bsl = new BluetoothSocketListener(bluetoothSocket);
                Thread messageListener = new Thread(bsl);
                messageListener.start();
                Log.d("BluetoothListener: ", "Success");
//                connectedThread = new ConnectedThread(bluetoothSocket);
//                connectedThread.run();

            }
            catch (IOException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }

            return;
        }
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
            Log.d("CONNECTTHREAD CANCEL: ", "CALLED");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String output) {

    }

    private class BluetoothSocketListener implements Runnable {

        private BluetoothSocket socket;

        public BluetoothSocketListener(BluetoothSocket socket) {
            this.socket = socket;
        }

        public void run() {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            try {
                InputStream instream = socket.getInputStream();
                int bytesRead = -1;
                String message = "";
                while (true) {
                    message = "";
                    if(instream.available() > 0) {
                        bytesRead = instream.read(buffer);

                        message = new String(buffer, 0, bytesRead);
                        String[] parsedMessage = message.split(",");
                        Message msgObj = mHandler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", message);
                        msgObj.setData(b);

                        mHandler.sendMessage(msgObj);

                        saveData(message);

//                        Log.d("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "");
//                        Log.d("Message: ", message);
//                        for(int i = 0; i < parsedMessage.length; i++) {
//                            Log.d("Parsed:", parsedMessage[i]);
//                        }
//                        Log.d("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "");
                    }
                    else{
                        SystemClock.sleep(3000);
                    }
                }
            } catch (IOException e) {
                Log.d("BLUETOOTH_COMMS", e.getMessage());
            }
        }
    }

    public void saveData(String data) {
        Log.d("dataArray:", data);

        String[] dataArray;
        dataArray = data.split(":");
        for(int i = 0; i < dataArray.length; i++) {
//            if(dataArray[i] == null) {
//                break;
//            }
            if(dataArray[i] != null && dataArray[i].charAt(0) == 'T') {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("fahrenheit", dataArray[i].substring(1));
                dbtools.addTemperature(map);
                Log.d("fahrenheit:", dataArray[i].substring(1));
                sharedPreferences.edit().putString("fahrenheit", String.valueOf(dataArray[i].substring(1))).apply();
                break;

            }
        }
        for(int i = 0; i < dataArray.length; i++) {
            if(dataArray[i] == null) {
                break;
            }
            if(dataArray[i].charAt(0) == 'S') {
                sharedPreferences.edit().putString("lastReceivedStepCount", dataArray[i].substring(1)).apply();
                Log.d("steps:", dataArray[i].substring(1));
                break;
            }
        }
        for(int i = 0; i < dataArray.length; i++) {
            if(dataArray[i] == null) {
                break;
            }
            if(dataArray[i].charAt(0) == 'H') {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("beats_per_minute", dataArray[i].substring(1));
                Log.d("beats_per_minute:", dataArray[i].substring(1));
                dbtools.addHeartbeat(map);
                sharedPreferences.edit().putString("beats_per_minute", String.valueOf(dataArray[i].substring(1))).apply();
                break;
            }
        }
        for(int i = 0; i < dataArray.length; i++) {
            if(dataArray[i] == null) {
                break;
            }
            if(dataArray[i].charAt(0) == 'E') {
                if(dataArray[i].charAt(1) == '1') {
                    sharedPreferences.edit().putString("emergencyNow", "1");
                }
            }
        }
    }

    public void sendTemperature(String fahrenheit){
//        String url = "https://health-monitor.herokuapp.com/api/v1/temperatures.json?";
//
//        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("fahrenheit", fahrenheit));
//        String paramString = URLEncodedUtils.format(params, "utf-8");
//
//        url += paramString;
//        HttpPost httpPost = new HttpPost(url);
//
//        new MyHttpPost(this).execute(httpPost);
    }
}
