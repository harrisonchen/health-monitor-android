package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class activity1 extends Activity implements MyAsyncResponse {

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    Button bluetoothButton;
    ListView listView;
    ArrayAdapter<String> deviceArrayAdapter;
    ArrayList<BluetoothDevice> deviceList;

    ConnectThread connectThread;
    boolean bluetoothSocketOpened = false;

    Handler mHandler;

    // Create a BroadcastReceiver for ACTION_FOUND
    final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                // Add the name and address to an array adapter to show in a ListView
                deviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                deviceArrayAdapter.notifyDataSetChanged();
                Log.d("BLUETOOTH PAIRING: ", device.getName());
            }
        }
    };

    TextView testview1;
    TextView testview2;
    TextView testview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity1);

        testview1 = (TextView) findViewById(R.id.testview1);
        testview2 = (TextView) findViewById(R.id.testview2);
        testview3 = (TextView) findViewById(R.id.testview3);

        mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("message");
                String[] parsedMessage = message.split(",");
                if (parsedMessage.length > 0)
                testview1.setText(parsedMessage[0]);
                if (parsedMessage.length > 1)
                testview2.setText(parsedMessage[1]);
                if (parsedMessage.length > 2)
                testview3.setText(parsedMessage[2]);

//                Log.d("MESSAGE FROM HANDLER:", msg.getData().getString("message"));
            }
        };

        bluetoothButton = (Button) findViewById(R.id.bluetoothButton);

        deviceList = new ArrayList<BluetoothDevice>();

        listView = (ListView) findViewById(R.id.listView);
        deviceArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(deviceArrayAdapter);

        if(bluetoothAdapter != null) {
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            IntentFilter filter2 = new IntentFilter(bluetoothAdapter.ACTION_DISCOVERY_STARTED);
//            IntentFilter filter3 = new IntentFilter(bluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy
//            registerReceiver(receiver, filter2);
//            registerReceiver(receiver, filter3);

            bluetoothButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!bluetoothAdapter.isEnabled()) {
                        connectBluetooth();
                    }
                    else {
                        discover();
                    }
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();
                Log.d("Device:", deviceArrayAdapter.getItem(i));
                connectThread = new ConnectThread(deviceList.get(i), bluetoothAdapter, mHandler);
                connectThread.run();
                bluetoothSocketOpened = true;
            }
        });
    }

    public void connectBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    public void discover(){
        if(bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(),"Bluetooth already discovering.\nStopping discovery..." , Toast.LENGTH_LONG).show();
            Log.d("Start Discovery: ", "already discovering");
        }
        else if(bluetoothAdapter.startDiscovery()) {
            Toast.makeText(getApplicationContext(),"Bluetooth discovering" , Toast.LENGTH_LONG).show();
            Log.d("Start Discovery: ", "true");
            deviceArrayAdapter.clear();
        }
    }

    public void sendRandomData(View view){
        String url = "https://health-monitor.herokuapp.com/api/v1/heartbeats.json?";

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        double randomVal = 80 + 25 * Math.random();
        params.add(new BasicNameValuePair("beats_per_minute", Double.toString(randomVal)));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        HttpPost httpPost = new HttpPost(url);

        new MyHttpPost(this).execute(httpPost);

        Intent i = new Intent(activity1.this, ChartDemo.class);
        startActivity(i);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT) {
            if(bluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(),"Bluetooth turned on" , Toast.LENGTH_LONG).show();
                discover();
            }
            else {
                Toast.makeText(getApplicationContext(),"Bluetooth failed to turn on" , Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity1, menu);
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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(receiver);
//        if(bluetoothSocketOpened){
//            connectThread.cancel();
//        }
        Log.d("ACTIVITY1 ON DESTROY: ", "CALLED");
    }

    public void goToMonitor(View view){
        Intent i = new Intent(activity1.this, Monitor.class);
        startActivity(i);
    }

    @Override
    public void processFinish(String output) {

    }
}
