package com.example.wmapp;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearMessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        if(messageEvent.getPath().equals("/my_path")){
            final String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);

            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }else{
            super.onMessageReceived(messageEvent);
        }
    }
}