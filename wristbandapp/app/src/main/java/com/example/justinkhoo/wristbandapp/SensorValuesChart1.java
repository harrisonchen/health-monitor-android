package com.example.justinkhoo.wristbandapp;

/**
 * Created by justinkhoo on 2/20/15.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.util.MathHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.util.Log;

import com.example.justinkhoo.wristbandapp.DBTools;
import com.example.justinkhoo.wristbandapp.chart.AbstractDemoChart;

/**
 * Temperature sensor demo chart.
 */
public class SensorValuesChart1 extends AbstractDemoChart1{
    private static final long HOUR = 3600 * 1000;

    private static final long DAY = HOUR * 24;

    private static final int HOURS = 24;

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Sensor data";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The temperature, as read from an outside and an inside sensors";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */

    public Intent execute(Context context, double[] array) {
        String[] titles = new String[] { "Inside", "Outside" };
        long now = Math.round(new Date().getTime() / DAY) * DAY;
        List<Date[]> x = new ArrayList<Date[]>();
        for (int i = 0; i < titles.length; i++) {
            Date[] dates = new Date[HOURS];
            for (int j = 0; j < HOURS; j++) {
                dates[j] = new Date(now - (HOURS - j) * HOUR);
            }
            x.add(dates);
        }


        double[] al = new double[]{21.2, 21.5, 21.7, 21.5, 21.4, 21.4, 21.3, 21.1, 20.6, 20.3, 20.2,
                19.9, 19.7, 19.6, 19.9, 20.3, 20.6, 20.9, 21.2, 21.6, 21.9, 22.1, 21.7, 21.5, };
        List<double[]> values = new ArrayList<double[]>();
        double[] arr = al;
        double[] arr1 = new double[24];
        Random random = new Random();
        int randomNum = random.nextInt() %50;


        values.add(array);
        values.add(new double[] { 21.2, 21.5, 21.7, 21.5, 21.4, 21.4, 21.3, 21.1, 20.6, 20.3, 20.2,
                19.9, 19.7, 19.6, 19.9, 20.3, 20.6, 20.9, 21.2, 21.6, 21.9, 22.1, 21.7, 21.5,  });
//   values.add(new double[] { 1.9, 1.2, 0.9, 0.5, 0.1, -0.5, -0.6, 10.1,
//        10.1, -1.8, -0.3, 1.4, 3.4, 4.9, 7.0, 6.4, 3.4, 2.0, 1.5, 0.9, -0.5,
//        10.1, -1.9, -2.5, -4.3, });

        int[] colors = new int[] { Color.GREEN, Color.BLUE };
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Sensor temperature", "Hour", "Celsius degrees",
                x.get(0)[0].getTime(), x.get(0)[HOURS - 1].getTime(), -5, 30, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        Intent intent = ChartFactory.getTimeChartIntent(context, buildDateDataset(titles, x, values),
                renderer, "h:mm a");
        return intent;
    }

}
