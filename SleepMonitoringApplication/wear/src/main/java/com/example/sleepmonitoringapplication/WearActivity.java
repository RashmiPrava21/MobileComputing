package com.example.sleepmonitoringapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.wear.widget.CircularProgressLayout;

public class WearActivity extends Activity implements
        CircularProgressLayout.OnTimerFinishedListener, View.OnClickListener {
    private CircularProgressLayout circularProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circularProgress =
                (CircularProgressLayout) findViewById(R.id.watch_view_stub);
        circularProgress.setOnTimerFinishedListener(this);
        circularProgress.setOnClickListener(this);
    }

    @Override
    public void onTimerFinished(CircularProgressLayout layout) {
        // User didn't cancel, perform the action
    }

    @Override
    public void onClick(View view) {
        if (view.equals(circularProgress)) {
            // User canceled, abort the action
            circularProgress.stopTimer();
        }
    }
}