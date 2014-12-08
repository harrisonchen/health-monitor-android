package com.example.justinkhoo.wristbandapp;



        import android.app.Activity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.TextView;

        import org.apache.http.client.methods.HttpGet;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.logging.Handler;
        import java.util.logging.LogRecord;


public class Monitor extends Activity implements  MyAsyncResponse {

    // HashMap<String, String> data;
    String stemp, sheartbeat, smessage;
    JSONObject dataJson;
    JSONArray dataJArray;
    TextView temp_msg, heartbeat_msg, message_msg;
    private int interval = 1000;

    private Handler myhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        temp_msg = (TextView) findViewById(R.id.temperature);
        heartbeat_msg = (TextView) findViewById(R.id.heartbeat);
        message_msg = (TextView) findViewById(R.id.message);
        getData();
    }



    public void getData() {
        //Creates the URL to call Get requests from the server with the added headers that
        //deal with authentication
        String url = "https://health-monitor.herokuapp.com/api/v1/status.json";
        HttpGet httpGet = new HttpGet(url);
        new MyHttpGet(this).execute(httpGet);
    }

    @Override
    public void processFinish(String output) {
        try {
            dataJson = new JSONObject(output);
            Log.d("---------->", output);
            fillList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Fills the list with the JSON values
    }

    public void fillList() {
        //Goes through the JSON of the wishArray to return each individual item and its characteristics
        // data.clear();
        try {
            stemp = dataJson.getString("average_temperature");
            sheartbeat = dataJson.getString("average_heartbeat");
            smessage = dataJson.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        temp_msg.setText("Temperature: " + stemp);
        heartbeat_msg.setText("Heartbeat: " + sheartbeat);
        message_msg.setText("Message : " + smessage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monitor, menu);
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
    public void goToUpdateData(View view){
        getData();
    }
}
