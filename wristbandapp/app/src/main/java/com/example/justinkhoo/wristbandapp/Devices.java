package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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


public class Devices extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    ListView deviceListView;
    ArrayAdapter<String> deviceArrayAdapter;
    ArrayList<BluetoothDevice> deviceList;
    ArrayList<String> arrayList;

    // Create a BroadcastReceiver for ACTION_FOUND
    final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                deviceList.add(device);
//                // Add the name and address to an array adapter to show in a ListView
//                deviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                deviceArrayAdapter.notifyDataSetChanged();
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        deviceList = new ArrayList<BluetoothDevice>();

        deviceListView = (ListView) findViewById(R.id.deviceListView);

        arrayList = new ArrayList<String>();
        arrayList.add("Example Device");
        deviceArrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_lobby_row, arrayList);

        deviceListView.setAdapter(deviceArrayAdapter);

        if(bluetoothAdapter != null) {
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy

            if(!bluetoothAdapter.isEnabled()) {
                connectBluetooth();
                discover();
            }
            else {
                discover();
            }
        }

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();
                Intent intent = new Intent(Devices.this, Lobby.class);
                startActivity(intent);

                finish();

                overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
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
            Toast.makeText(getApplicationContext(), "Bluetooth already discovering.\nStopping discovery...", Toast.LENGTH_LONG).show();
            Log.d("Start Discovery: ", "already discovering");
        }
        else if(bluetoothAdapter.startDiscovery()) {
            Toast.makeText(getApplicationContext(),"Bluetooth discovering" , Toast.LENGTH_LONG).show();
            Log.d("Start Discovery: ", "true");
//            deviceArrayAdapter.clear();
        }
    }

    public void manualTransitionOut() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(Devices.this, Lobby.class);
                startActivity(i);

                finish();

                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(receiver);
        Log.d("ACTIVITY1 ON DESTROY: ", "CALLED");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.devices, menu);
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
}
