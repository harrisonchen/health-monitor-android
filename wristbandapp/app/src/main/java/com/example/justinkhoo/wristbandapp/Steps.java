package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.util.Random;


public class Steps extends Activity {

    TextView stepsTextView;
    Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        stepsTextView = (TextView) findViewById(R.id.stepsTextView);

        clearButton = (Button) findViewById( R.id.clearButton );
        clearButton.setTypeface(font);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepsTextView.setText("0");
            }
        });
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
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[] {20, 30, 15, 0});
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.GREEN);
        renderer.addSeriesRenderer(r);
        return renderer;
    }
    private void setChartSettings(XYMultipleSeriesRenderer renderer) {
        renderer.setChartTitle("Chart demo");
        renderer.setXTitle("x values");
        renderer.setYTitle("y values");
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
        CategorySeries series = new CategorySeries("Demo series ");
        for (int k = 0; k < nr; k++) {
            series.add(60 + r.nextInt() % 20);
        }
        dataset.addSeries(series.toXYSeries());

        CategorySeries series1 = new CategorySeries("Demo series ");
        for (int k = 0; k < nr; k++) {
            series.add(60 + r.nextInt() % 20);
        }
        dataset.addSeries(series1.toXYSeries());
        //}
        return dataset;
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
