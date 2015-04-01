package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.HashMap;
import java.util.Random;


import android.support.v4.app.NotificationCompat;


public class Steps extends Activity {
    TextView stepsTextView;
    Button clearButton;
    DBTools dbtools = new DBTools(this);

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        sharedPreferences = this.getSharedPreferences("com.example.justinkhoo.wristbandapp", Context.MODE_PRIVATE);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        stepsTextView = (TextView) findViewById(R.id.stepsTextView);

        stepsTextView.setText(sharedPreferences.getString("stepCount", "0"));

        clearButton = (Button) findViewById( R.id.clearButton );
        clearButton.setTypeface(font);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepsTextView.getText().toString().equals("0")) {
                    sharedPreferences.edit().putString("stepCount", "0").apply();
                    return;
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("step_count", stepsTextView.getText().toString());
                dbtools.addSteps(map);
                stepsTextView.setText("0");
                sharedPreferences.edit().putString("stepCount", "0").apply();
            }
        });
    }

    public void goToStepGoals(View view) {
        Intent intent = new Intent(this, StepGoals.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.steps, menu);
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
    public XYMultipleSeriesRenderer getBarDemoRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(20);
        renderer.setChartTitleTextSize(24);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(20);
        renderer.setLabelsColor(Color.RED);
        renderer.setMargins(new int[] {20, 30, 15, 0});

        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE); //inside
        renderer.setMarginsColor(Color.WHITE);  //outside
        renderer.setPanEnabled(false, false);    //scroll
        renderer.setZoomEnabled(false,false); //zoom
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.GREEN);
        renderer.addSeriesRenderer(r);
        return renderer;
    }
    private void setChartSettings(XYMultipleSeriesRenderer renderer) {
        renderer.setChartTitle("");
        renderer.setXTitle("temperature");
        renderer.setYTitle("time");
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(20.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(210);
    }

    private XYMultipleSeriesDataset getBarDemoDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = 10;
        Random r = new Random();
        // for (int i = 0; i < 1; i++) {
        CategorySeries series = new CategorySeries(""); //"Demo series"
        for (int k = 0; k < nr; k++) {
            series.add(60 + r.nextInt() % 20);
        }
        dataset.addSeries(series.toXYSeries());

        CategorySeries series1 = new CategorySeries("");
        for (int k = 0; k < nr; k++) {
            series.add(60 + r.nextInt() % 20);
        }
        dataset.addSeries(series1.toXYSeries());
        //}
        return dataset;
    }

    // Add app running notification
    public void addNotification(View view) {

        buildNotification("OneBand", "You have reached your step goal!", Steps.class, 1);
        buildNotification("OneBand", "You should cool down", Temperatures.class, 2);

//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("OneBand Health")
//                        .setContentText("You have reached your step goal!");
//
//        Intent notificationIntent = new Intent(this, Steps.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
//
//        // Add as notification
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(1, builder.build());
     }

    public void buildNotification(String title, String content, Class classname, Integer id) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND);

        Intent notificationIntent = new Intent(this, classname);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
    }

    // Remove notification
    private void removeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
    }

    public void goToStepChart(View view){
        SensorValuesChart1 mychart = new SensorValuesChart1();

//        double [] array = toArray(al1);
//        Log.d("arry size is : ", String.valueOf(array.length));
//        Intent i = mychart.execute(this, array);
//        startActivity(i);
        XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
        setChartSettings(renderer);
        Intent i= ChartFactory.getBarChartIntent(this, getBarDemoDataset(), renderer, BarChart.Type.DEFAULT);
        startActivity(i);
    }
}
