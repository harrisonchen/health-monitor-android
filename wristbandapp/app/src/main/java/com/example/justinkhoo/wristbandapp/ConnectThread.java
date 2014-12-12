package com.example.justinkhoo.wristbandapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ConnectThread extends Thread implements MyAsyncResponse {
    private BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final BluetoothAdapter bluetoothAdapter;

    public ConnectedThread connectedThread;

    private UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter) {

        BluetoothSocket tmp = null;
        bluetoothDevice = device;
        bluetoothAdapter = adapter;

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
//                    bytesRead = instream.read(buffer);
                    if(instream.available() > 0) {
//                        while ((bytesRead==bufferSize)&&(buffer[bufferSize-1] != 0)) {
//                            message = message + new String(buffer, 0, bytesRead);
//                            bytesRead = instream.read(buffer);
//                        }

                        bytesRead = instream.read(buffer);

                        message = new String(buffer, 0, bytesRead);
                        if(Float.valueOf(message) > 10){
                            Log.d("Message: ", message);
                            sendTemperature(message);

                        }
//                        socket.getInputStream();
                    }
                    else{
                        SystemClock.sleep(100);
                    }

//                    message = "";
//                    bytesRead = instream.read(buffer);
//                    if (bytesRead != -1) {
//                        while ((bytesRead==bufferSize)&&(buffer[bufferSize-1] != 0)) {
//                            message = message + new String(buffer, 0, bytesRead);
//                            bytesRead = instream.read(buffer);
//                        }
//                        message = message + new String(buffer, 0, bytesRead - 1);
//
////                        handler.post(new MessagePoster(textView, message));
//                        socket.getInputStream();
//                    }
                }
            } catch (IOException e) {
                Log.d("BLUETOOTH_COMMS", e.getMessage());
            }
        }
    }

    public void sendTemperature(String fahrenheit){
        String url = "https://health-monitor.herokuapp.com/api/v1/temperatures.json?";

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("fahrenheit", fahrenheit));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        HttpPost httpPost = new HttpPost(url);

        new MyHttpPost(this).execute(httpPost);
    }
}
