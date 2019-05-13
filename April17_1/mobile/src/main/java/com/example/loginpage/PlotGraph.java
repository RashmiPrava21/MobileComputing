package com.example.loginpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class PlotGraph extends AppCompatActivity {

    ArrayList<List<Object>> graphValues = new ArrayList<List<Object>>();

    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_graph);

        Bundle b = getIntent().getExtras();
        graphValues = (ArrayList<List<Object>>) b.getSerializable("retrieveValues");
        Log.d("Received", "Retrieving array in Graph activity");

        int size = graphValues.size();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < size; i++){
            series.appendData(new DataPoint(i, (Double) graphValues.get(i).get(1)), true, 10000);
        }

        graph.addSeries(series);
    }
}
