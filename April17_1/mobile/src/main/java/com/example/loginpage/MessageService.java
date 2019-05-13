package com.example.loginpage;

import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageService extends WearableListenerService {

//    DatabaseReference getDataRef;
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

//        getDataRef = FirebaseDatabase.getInstance().getReference("UserAvailable");
        //If the message’s path equals "/my_path"...//
        if (messageEvent.getPath().equals("/my_path")) {
            int high_intensity = 0;
            int low_intensity = 0;
            long total_sleep_time;

            Toast.makeText(getApplicationContext(), "Data received from Watch", Toast.LENGTH_SHORT).show();
            try(ByteArrayInputStream bos = new ByteArrayInputStream(messageEvent.getData());
                ObjectInputStream oos = new ObjectInputStream(bos)) {
                Object o = oos.readObject();
                System.out.println("Object Type: " +(o instanceof ArrayList));
                if ( o instanceof ArrayList ) {
                    ArrayList<List<Object>> list = (ArrayList<List<Object>>)o;
                    System.out.println(list);

                    Toast.makeText(getApplicationContext(), "Updating Database!", Toast.LENGTH_SHORT).show();

                    DateFormat start_df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
                    DateFormat end_df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                    start_df = (DateFormat) list.get(0).get(0);
                    Date date1 = start_df.parse((String) list.get(0).get(0));

                    for (int i = 0; i < list.size(); i++) {
                        float temp_rms_value = (float) list.get(i).get(1);
                        if (temp_rms_value >= 3.00) {
                            high_intensity++;
                        } else {
                            if (temp_rms_value >= 1.5 && temp_rms_value < 3.00){
                                low_intensity++;
                            }
                        }
                        end_df = (DateFormat) list.get(i).get(0);
                    }

                    // total_sleep_time = end_df - start_df;

                    int total_movements = low_intensity + high_intensity;

                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    Users temp = new Users(userEmail, list, high_intensity, low_intensity, total_movements);
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
