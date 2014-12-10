package com.example.justinkhoo.wristbandapp;

import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public ConnectedThread(BluetoothSocket socket) {
        bluetoothSocket = socket;
        InputStream in = null;
        OutputStream out = null;

        Log.d("AWESOME", "");

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = in;
        outputStream = out;
    }

    public void run() {
        byte[] buffer = new byte[256];
        int bytes;

        while(true){
            try {
                if(inputStream.available() > 0) {
                    bytes = inputStream.read(buffer);
                    String message = new String(buffer, 0, bytes);
                    if(Float.valueOf(message) > 10){
                        Log.d("Message: ", message);
                    }
                }
                else{
                    SystemClock.sleep(100);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
