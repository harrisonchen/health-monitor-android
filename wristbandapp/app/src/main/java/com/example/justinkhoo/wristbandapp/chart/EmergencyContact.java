package com.example.justinkhoo.wristbandapp.chart;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.justinkhoo.wristbandapp.DBTools;
import com.example.justinkhoo.wristbandapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EmergencyContact extends Activity {
    DBTools dbTools = new DBTools(this);
    EditText nameEditText;
    EditText phoneEditText;
    Button addToContactButton;
    ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        nameEditText = (EditText) findViewById(R.id.addname);
        phoneEditText = (EditText) findViewById(R.id.addphone);
        addToContactButton = (Button) findViewById(R.id.addToContactBtn);
        contactListView = (ListView) findViewById(R.id.contactList);
        ArrayList<String> items = new ArrayList<String>();
        int size = dbTools.getEmergencyContacts().size();
        for(int i =0; i< size; i++) {
            items.add(dbTools.getEmergencyContacts().get(i).get("name") +" ("+dbTools.getEmergencyContacts().get(i).get("phone")+")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_lobby_row, items);
        contactListView.setAdapter(adapter);
    }
    private void invalidEntryAlert(String message) {
        /// change to current class
        AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyContact.this);

        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }
    public void addToContacts(View view){
        if(nameEditText.getText().toString().equals("") || phoneEditText.getText().toString().equals("")) {
             invalidEntryAlert("name and phone number cannot be black");
        }else{
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", name);
            map.put("phone", phone);
            dbTools.addEmergencyContacts(map);

            ArrayList<String> items = new ArrayList<String>();
            int size = dbTools.getEmergencyContacts().size();
            for(int i =0; i< size; i++) {
                items.add(dbTools.getEmergencyContacts().get(i).get("name") +" ("+dbTools.getEmergencyContacts().get(i).get("phone")+")");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_lobby_row, items);
            contactListView.setAdapter(adapter);
            nameEditText.setText("");
            phoneEditText.setText("");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
