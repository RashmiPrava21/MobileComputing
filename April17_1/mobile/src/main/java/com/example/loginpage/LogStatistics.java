package com.example.loginpage;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogStatistics extends AppCompatActivity {

    DatabaseReference getDataRef;
    private List<Users> usersList;
    String getEmailFAuth;
    ArrayList<List<Object>> getData = new ArrayList<List<Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_statistics);
    }

    @Override
    protected void onStart(){
        super.onStart();

        getDataRef = FirebaseDatabase.getInstance().getReference("UsersAvailable");
        getEmailFAuth = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            getDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot user : dataSnapshot.getChildren()){
                    usersList.add(user.getValue(Users.class));
                }

                for(Users userFound : usersList){
                    String getEmailList = userFound.getEmail();

                    if(getEmailList.equals(getEmailFAuth)){
                        getData = userFound.getUserData();
                        Toast.makeText(getApplicationContext(), "Check the data", Toast.LENGTH_SHORT).show();
                        System.out.println(getData);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
