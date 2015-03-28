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

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Notification;
import android.support.v4.app.NotificationCompat;

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
        renderer.setPanEnabled(true,false);    //scroll
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
//    public void sendNotification(View view) {
//
//        // Use NotificationCompat.Builder to set up our notification.
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//        //icon appears in device notification bar and right hand corner of notification
//       // builder.setSmallIcon(R.drawable.ic_stat_notification);
//
//        // This intent is fired when notification is clicked
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javatechig.com/"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        // Set the intent that will fire when the user taps the notification.
//        builder.setContentIntent(pendingIntent);
//
//        // Large icon appears on the left of the notification
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
//
//        // Content title, which appears in large type at the top of the notification
//        builder.setContentTitle("Notifications Title");
//
//        // Content text, which appears in smaller text below the title
//        builder.setContentText("Your notification content here.");
//
//        // The subtext, which appears under the text on newer devices.
//        // This will show-up in the devices with Android 4.2 and above only
//        builder.setSubText("Tap to view documentation about notifications.");
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        // Will display the notification in the notification bar
//        notificationManager.notify(1, builder.build());
//    }
// Add app running notification
//        private void addNotification() {
//
//            NotificationCompat.Builder builder =
//                    new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle("Notifications Example")
//                            .setContentText("This is a test notification");
//
//            Intent notificationIntent = new Intent(this, Steps.class);
//            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            builder.setContentIntent(contentIntent);
//
//            // Add as notification
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(1, builder.build());
//        }
//
//            // Remove notification
//            private void removeNotification() {
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.cancel(2);
//            }
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
