package com.example.loginpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;

public class StopSleep extends AppCompatActivity {

    private Button button;
    private DelayedConfirmationView delayedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_sleep);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.delayedView);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                button = stub.findViewById(R.id.button);
                delayedView = stub.findViewById(R.id.delayedView);
                delayedView.setTotalTimeMs(3000);
                showOnlyButton();
            }
        });
    }

    public void beginCountdown(View view) {
        button.setVisibility(View.GONE);
        delayedView.setVisibility(View.VISIBLE);
        delayedView.setListener(new DelayedConfirmationView.DelayedConfirmationListener() {
            @Override
            public void onTimerFinished(View view) {
                showOnlyButton();
            }
            @Override
            public void onTimerSelected(View view) {
            }
        });
        delayedView.start();
    }

    public void showOnlyButton() {
        button.setVisibility(View.VISIBLE);
        delayedView.setVisibility(View.GONE);
    }
}
