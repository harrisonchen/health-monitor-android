package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class activity1 extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    Button bluetoothButton;
    ListView listView;
    ArrayAdapter<String> deviceArrayAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND
    final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                deviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                deviceArrayAdapter.notifyDataSetChanged();
                Log.d("BLUETOOTH PAIRING: ", device.getName());
            }
//            if (bluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
//                Log.d("Start Discovery: ", "started");
//            }
//            if (bluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                Log.d("Start Discovery: ", "finished");
//                bluetoothAdapter.cancelDiscovery();
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity1);

        bluetoothButton = (Button) findViewById(R.id.bluetoothButton);

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
                        discover();
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
                Log.d("Device:", deviceArrayAdapter.getItem(i));
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
        else {
            Toast.makeText(getApplicationContext(),"Bluetooth discovery failed" , Toast.LENGTH_LONG).show();
            Log.d("Start Discovery: ", "false");
        }
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
        unregisterReceiver(receiver);
    }

}
