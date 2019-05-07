package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomePage extends AppCompatActivity implements Serializable {

    private TextView mTextMessage;
    private Button Sleep_Button;
    private TextView connectText;
    protected Handler myHandler;
    protected Handler connectedHandler;
    private Boolean buttonStatus = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    Intent intent = new Intent(getApplicationContext(), AccountPage.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try
        {
            this.getSupportActionBar().hide();

        }
        catch (NullPointerException e){}

        setContentView(R.layout.home_page);

        Sleep_Button = (Button) findViewById(R.id.start_sleep_button);

        connectText = (TextView) findViewById(R.id.connectingNotification);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                messageText(stuff.getString("messageText"));
                return true;
            }
        });

        Sleep_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                connectText.setText(message);

                //Sending a message can block the main UI thread, so use a new thread//

                new HomePage.NewThread("/my_path", message).start();
                buttonStatus = true;

            }
        });
    }

    public void messageText(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            connectText.append("\n" + newinfo);
        }
    }

    /*public void sendmessage(String messageText) {
        Bundle bundle = new Bundle();
        bundle.putString("messageText", messageText);
        Message msg = myHandler.obtainMessage();
        msg.setData(bundle);
        myHandler.sendMessage(msg);

    }*/

    /*public class ConnectedHandler implements Runnable{
        public void run(){
            Toast.makeText(getApplicationContext(), "Connected to Wearable Device!", Toast.LENGTH_LONG).show();
        }
    }*/

    class NewThread extends Thread {
        String path;
        String message;

        //Constructor for sending information to the Data Layer//

        NewThread(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {
            //Retrieve the connected devices, known as nodes//
            Task<List<Node>> wearableList = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();

            try {
                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            //Send the message//
                            Wearable.getMessageClient(HomePage.this).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        //Block on a task and get the result synchronously//
                        Integer result = Tasks.await(sendMessageTask);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Connected to Wearable Device!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        if(buttonStatus == true){
                            Sleep_Button.setClickable(false);
                        }
                        //if the Task fails, then…..//
                    } catch (ExecutionException exception) {
                        //TO DO: Handle the exception//
                    } catch (InterruptedException exception) {
                        //TO DO: Handle the exception//
                    }
                }
            } catch (ExecutionException exception) {
                //TO DO: Handle the exception//
            } catch (InterruptedException exception) {
                //TO DO: Handle the exception//
            }
        }
    }


}
