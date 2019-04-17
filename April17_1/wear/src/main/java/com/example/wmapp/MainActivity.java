package com.example.wmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
//    Button talkButton;
    int receivedMessageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
//        talkButton = (Button) findViewById(R.id.talkClick);

        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        // Enables Always-on
        setAmbientEnabled();


    }

    public class Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            String onMessageReceived = "Just received a message from handheld" + receivedMessageNumber++;
            mTextView.setText(onMessageReceived);

        }
    }
}
