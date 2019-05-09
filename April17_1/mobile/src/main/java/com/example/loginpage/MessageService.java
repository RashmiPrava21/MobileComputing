package com.example.loginpage;

import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        //If the message’s path equals "/my_path"...//
        if (messageEvent.getPath().equals("/my_path")) {
            Toast.makeText(getApplicationContext(), "Data received from Watch", Toast.LENGTH_SHORT).show();
            try(ByteArrayInputStream bos = new ByteArrayInputStream(messageEvent.getData());
                ObjectInputStream oos = new ObjectInputStream(bos)) {
                Object o = oos.readObject();
                System.out.println("Object Type: " +(o instanceof ArrayList));
                if ( o instanceof ArrayList ) {
                    ArrayList<List<Object>> list = (ArrayList<List<Object>>)o;
                    System.out.println(list);

                } else {
                    System.out.println("----------------------------"+ o.getClass().getName() +"-------------------------------");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }


}
