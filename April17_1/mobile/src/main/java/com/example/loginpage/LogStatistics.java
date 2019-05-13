package com.example.loginpage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class LogStatistics extends AppCompatActivity {

    private Button plotGraph;
    DatabaseReference getDataRef;
    private List<Users> usersList;
    String getEmailFAuth;
    ArrayList<List<Object>> getData = new ArrayList<List<Object>>();
    private EditText sleep;
    private EditText movements;
    private EditText high;
    private EditText low;

    List<String> getTm = new ArrayList<>();
    String getTotal;
    String getHigh;
    String getLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_statistics);

        usersList = new ArrayList<Users>();
        plotGraph = (Button) findViewById(R.id.graph);
        sleep = (EditText) findViewById(R.id.totalSleep);
        movements = (EditText) findViewById(R.id.totolMovements);
        high = (EditText) findViewById(R.id.highIntensity);
        low = (EditText) findViewById(R.id.lowIntensity);

        plotGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("retrieveValues", (Serializable) getData);
                Intent intent = new Intent(LogStatistics.this, PlotGraph.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
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
                        getTotal = userFound.getTotalMovements();
                        getHigh = userFound.getHighIntensity();
                        getLow = userFound.getLowIntensity();
                        getTm = userFound.getTime();

                        /*sleep.setText(getTm.toString());
                        movements.setText(getTotal);
                        high.setText(getHigh);
                        low.setText(getLow);*/

//                        Toast.makeText(getApplicationContext(), "Check the data", Toast.LENGTH_SHORT).show();
                        System.out.println(getTm);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sleep.setText(getTm.toString());
        movements.setText(getTotal);
        high.setText(getHigh);
        low.setText(getLow);
    }
}
