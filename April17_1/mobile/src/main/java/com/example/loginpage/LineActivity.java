package com.example.loginpage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LineActivity extends AppCompatActivity {

    private static final String TAG = "LineActivity";

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        mChart = (LineChart) findViewById(R.id.linechart);

   //     mChart.setOnChartGestureListener(LineActivity.this);
     //   mChart.setOnChartValueSelectedListener(LineActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, (int) 60f));
        yValues.add(new Entry(1, (int) 50f));
        yValues.add(new Entry(2, (int) 70f));
        yValues.add(new Entry(3, (int) 30f));
        yValues.add(new Entry(4, (int) 50f));
        yValues.add(new Entry(5, (int) 60f));
        yValues.add(new Entry(6, (int) 65f));
        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

        set1.setFillAlpha(110);

        set1.setColor(Color.BLUE);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data);
    }
}
