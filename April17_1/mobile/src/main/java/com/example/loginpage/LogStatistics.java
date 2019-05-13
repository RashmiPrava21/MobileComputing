package com.example.loginpage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class LogStatistics extends AppCompatActivity {

//    private TextView text;
    private Button log;
    DatabaseReference getDataRef;
    private List<Users> usersList;
    String getEmailFAuth;
    ArrayList<List<Object>> getData = new ArrayList<List<Object>>();
    private int getTotalMovements;
    private int getHighIntensity;
    private int getLowIntensity;

//    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_statistics);

        getDataRef = FirebaseDatabase.getInstance().getReference("UsersAvailable");
        getEmailFAuth = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        text = (TextView) findViewById(R.id.displayData);
        log = (Button) findViewById(R.id.logbutton);

        /*getDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    usersList.add(user.getValue(Users.class));
                }

                for(Users userFound : usersList){
                    String getEmailList = userFound.getEmail();

                    if(getEmailList.equals(getEmailFAuth)){
                        getData = userFound.getUserData();
                        getHighIntensity = userFound.getHighIntensity();
                        getLowIntensity = userFound.getLowIntensity();
                        getTotalMovements = userFound.getTotalMovements();
                        Toast.makeText(getApplicationContext(), "Check the data", Toast.LENGTH_SHORT).show();
                        System.out.println(getTotalMovements);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

//        text.setText(getTotalMovements);

        /*ListView lv = (ListView) findViewById(R.id.listview);
        generateListContent();
        // lv.setAdapter(new MyListAdaper(this, R.layout.list_item, data));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LogStatistics.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

   /* private void generateListContent() {
        for(int i = 0; i < 55; i++) {
            data.add("This is row number " + i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

   /* private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    public class ViewHolder {

        ImageView thumbnail;
        TextView title;
        Button button;
    }*/

   /* @Override
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
    }*/
}
