package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Temperatures extends Activity {

    DBTools dbTools = new DBTools(this);
    Handler mHandler;
    TemperatureHandlerThread temperatureHandlerThread;
    TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatures);

        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);

        mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("message");

                temperatureTextView.setText(message + "\u00B0F");
            }
        };

        temperatureHandlerThread = new TemperatureHandlerThread(mHandler, this);
        temperatureHandlerThread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.temperatures, menu);
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
        renderer.setPanEnabled(false ,false);    //scroll
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
        renderer.setChartTitle("Temperature");
        renderer.setXTitle("time");
        renderer.setYTitle("farenheit");
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(10.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(210);
    }

    private XYMultipleSeriesDataset getBarDemoDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = 10;
        Random r = new Random();
        // for (int i = 0; i < 1; i++) {
        CategorySeries series = new CategorySeries(""); //Demo series
        ArrayList<String> al = new ArrayList<String>();
//        int counter = 0;
//        while(!dbTools.getTemperature().get(counter).get("fahrenheit").equals("")) {
////            Log.d("testing 1", "" + counter);Log.d("testing 1", "" + counter);Log.d("testing 1", "" + counter);Log.d("testing 1", "" + counter);Log.d("testing 1", "" + counter);
//            al.add(dbTools.getTemperature().get(counter).get("fahrenheit"));
//            counter++;
//        }
        for (int k = 0; k < nr; k++) {
//            series.add(60 + r.nextInt() % 20);
//             if(al.get(k) != null){
            if(k >= dbTools.getTemperature().size())
                series.add(0.0);
            else
                series.add(Double.parseDouble(dbTools.getTemperature().get(k).get("fahrenheit")));
//             }else{
//                 series.add(0);
//             }
        }
        dataset.addSeries(series.toXYSeries());

        CategorySeries series1 = new CategorySeries("");//Demo series
        for (int k = 0; k < nr; k++) {
//            series.add(60 + r.nextInt() % 20);
            series.add(0);
        }
        dataset.addSeries(series1.toXYSeries());
        //}
        return dataset;
    }

    public void goToTempChart(View view){
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