package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Heartrate extends Activity {

    TextView heartbeatTextView;
    TextView minTextView;
    DBTools dbtools = new DBTools(this);
    Handler mHandler;
    HeartrateHandlerThread heartrateHandlerThread;
    GraphicalView graphicalView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        heartbeatTextView = (TextView) findViewById( R.id.heartbeatTextView );
        minTextView = (TextView) findViewById(R.id.minTextView);
        minTextView.setTypeface(font);

        mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("message");

                heartbeatTextView.setText(message);
            }
        };

        heartrateHandlerThread = new HeartrateHandlerThread(mHandler, this);
        heartrateHandlerThread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.heartrate, menu);
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
        renderer.setChartTitle("Heart Beats");
        renderer.setXTitle("time");
        renderer.setYTitle("bpm");
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(10.5);
        renderer.setYAxisMin(getMin());
        renderer.setYAxisMax(getMax()+10);
    }
    private Double getMin(){
        double min = getMax()-20;
        for (int i = 0; i < dbtools.getHeartbeat().size(); i++) {
            if(Double.parseDouble(dbtools.getHeartbeat().get(i).get("beats_per_minute")) < min){
                min = Double.parseDouble(dbtools.getHeartbeat().get(i).get("beats_per_minute")) -5;
            }
        }
        return min;
    }
    private Double getMax() {
        double max = 0;
        for (int i = 0; i < dbtools.getHeartbeat().size(); i++) {
            if(Double.parseDouble(dbtools.getHeartbeat().get(i).get("beats_per_minute")) > max){
                max = Double.parseDouble(dbtools.getHeartbeat().get(i).get("beats_per_minute"));
            }
        }
        if(max < 50){
            max = 50;
        }
        return max;
    }

    private XYMultipleSeriesDataset getBarDemoDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = 10;
        Random r = new Random();
        // for (int i = 0; i < 1; i++) {
        CategorySeries series = new CategorySeries(""); //"Demo series"
        for (int k = 0; k < nr; k++) {
            if(k >= dbtools.getHeartbeat().size())
                series.add(0.0);
            else
                series.add(Double.parseDouble(dbtools.getHeartbeat().get(k).get("beats_per_minute")));//series.add(60 + r.nextInt() % 10);
        }
        dataset.addSeries(series.toXYSeries());

        CategorySeries series1 = new CategorySeries(""); //"Demo series"
        for (int k = 0; k < nr; k++) {
            series.add(0);
        }
        dataset.addSeries(series1.toXYSeries());
        //}
        return dataset;
    }

    Timer timer;
    TimerTask timertask;
    final Handler handler = new Handler();

    int count = 0;
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timertask, 5000, 5000);
    }
    public void initializeTimerTask() {
        timertask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp
                        graphicalView.repaint();
                        Log.d("timer task execution", String.valueOf(count++));
                    }
                });
            }
        };
    }

    public void goToHeartChart(View view){
        SensorValuesChart1 mychart = new SensorValuesChart1();

//        double [] array = toArray(al1);
//        Log.d("arry size is : ", String.valueOf(array.length));
//        Intent i = mychart.execute(this, array);
//        startActivity(i);
        XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
        setChartSettings(renderer);
        Intent i= ChartFactory.getBarChartIntent(this, getBarDemoDataset(), renderer, BarChart.Type.DEFAULT);
        startActivity(i);

        renderer.setInScroll(true);
//        graphicalView = ChartFactory.getBarChartView(this, getBarDemoDataset(), renderer, BarChart.Type.DEFAULT);
//        startTimer();

    }

}