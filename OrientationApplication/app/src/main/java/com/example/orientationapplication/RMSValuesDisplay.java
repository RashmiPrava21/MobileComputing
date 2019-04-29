package com.example.orientationapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RMSValuesDisplay extends AppCompatActivity implements View.OnClickListener {

//    List<Float> list = new ArrayList<Float>();
    ArrayList<List<Float>> retrieved = new ArrayList<List<Float>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmsvalues_display);

        Button graphButton = findViewById(R.id.PlotData);
        graphButton.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        retrieved = (ArrayList<List<Float>>) b.getSerializable("retrieveValues");
        Log.d("Received", "Retrieving array");

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_listview, retrieved);

        ListView listView = (ListView) findViewById(R.id.window_list);
        listView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {

        Bundle b = new Bundle();
        b.putSerializable("retrieveValues", (Serializable) retrieved);
        Intent intent = new Intent(RMSValuesDisplay.this, PlotGraph.class);
        intent.putExtras(b);
        startActivity(intent);

    }
}
