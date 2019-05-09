package com.example.loginpage;

import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class MessageService extends WearableListenerService {

//    DatabaseReference getDataRef;
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

//        getDataRef = FirebaseDatabase.getInstance().getReference("UserAvailable");
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

                    Toast.makeText(getApplicationContext(), "Updating Database!", Toast.LENGTH_SHORT).show();

                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    Users temp = new Users(userEmail, list);
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("UsersAvailable");
                    dbRef.child(userID).setValue(temp);

                    Toast.makeText(getApplicationContext(), "Successfully updated Database", Toast.LENGTH_SHORT).show();

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
