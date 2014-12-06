package com.example.justinkhoo.wristbandapp;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

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
        byte[] buffer = new byte[1024];
        int bytes;

        while(true){
            try {
                Log.d("WEEEE", "");
                bytes = inputStream.read(buffer);
                String string = "";
                for(int i = 0; i < 1024; ++i) {
                    if(buffer[i] != 0) {
                        string += buffer[i] + ":";
                    }
                }
                Log.d("Message:", String.valueOf(string));
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
