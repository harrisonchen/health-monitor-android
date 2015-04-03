package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Random;


public class Heartrate extends Activity {

    TextView heartbeatTextView;
    DBTools dbtools = new DBTools(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);
        for(int i = 50; i <= 100; i = i + 2) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("beats_per_minute", String.valueOf(i));
            dbtools.addHeartbeat(map);
        }
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        heartbeatTextView = (TextView) findViewById( R.id.heartbeatTextView );
        heartbeatTextView.setTypeface(font);

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
    }
}
