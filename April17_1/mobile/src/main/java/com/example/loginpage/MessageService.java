package com.example.loginpage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        //If the message’s path equals "/my_path"...//
        if (messageEvent.getPath().equals("/my_path")) {

            ByteArrayInputStream bos = new ByteArrayInputStream(messageEvent.getData());
            // bos.read();

            int num;
            while ((num = bos.read()) != -1) {
                Log.d("Printing Data", "" + num);
            }

            ObjectInputStream oos;
            //Log.d("Data Received", "%s"+bos);
            Toast.makeText(getApplicationContext(), "Data Received", Toast.LENGTH_LONG).show();
            /*try {
                oos = new ObjectInputStream(bos);
                //byte[] readData = oos.read(bos);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            //...retrieve the message//
            final String message = new String(messageEvent.getData());

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);

            //Broadcast the received Data Layer messages locally//
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }


}
