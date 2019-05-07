package com.example.loginpage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import com.google.android.gms.wearable.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class WearConnection extends WearableActivity
        implements View.OnClickListener, SensorEventListener, Serializable /*GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks*/ {

    private Handler myHandler_;

    GoogleApiClient googleApiClient;

    private TextView mTextView;
    private Button startButton;
    private Button stopButton;
    boolean buttonStatus = false;

    private static final String TAG = WearConnection.class.getSimpleName();

    private SensorManager sensorManager_;
    private Sensor accelerometer;

    private double rmsValue = 0.0f;
    private long currentDateTime;
    private Date currentDate;

    private Float accXwoG = (float) 0.0;
    private Float accYwoG = (float) 0.0;
    private Float accZwoG = (float) 0.0;
    String PATH="/sendreceive";

    Formatter fmt = new Formatter();
    ArrayList<List<Object>> newRMSMilliValues = new ArrayList<List<Object>>();


//    Button talkButton;
//    int receivedMessageNumber = 1;
//    int sentMessageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_connection);

        myHandler_ = new Handler();

        /*googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        googleApiClient.connect();*/

        mTextView = (TextView) findViewById(R.id.connectivityNotification);
        startButton = (Button) findViewById(R.id.startSleep);
        stopButton = (Button) findViewById(R.id.stopButton);

        sensorManager_ = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager_.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer == null) {
            Log.i(TAG, "Accelerometer not supported");
        }

        startButton.setOnClickListener(this);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager_.unregisterListener(WearConnection.this);
                Log.d("RMS array values", "array:" + newRMSMilliValues);
                /*SendMessage sendMessage_ = new SendMessage("/my_path", newRMSMilliValues);
                myHandler_.post(sendMessage_);*/
                // new SendMessage("/my_path", newRMSMilliValues).start();
                SendMessage sendMessage_ = new SendMessage("/my_path", newRMSMilliValues);
                Thread thread = new Thread(sendMessage_);
                thread.start();
            }
        });

        // StartStopButton startStopButton = new StartStopButton();
        // myHandler_.post(startStopButton);
//        startButton.setOnClickListener(this);
//        talkButton = (Button) findViewById(R.id.talkClick);

        /*talkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onClickMessage = "I just sent the handheld a message " + sentMessageNumber++;
                mTextView.setText(onClickMessage);

                //Use the same path//

                String datapath = "/my_path";
                new SendMessage(datapath, onClickMessage).start();

            }
        });*/

        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);

        // Enables Always-on
        setAmbientEnabled();
    }

    /*private void sendMessage() {
        if(googleApiClient.isConnected()) {
            new Thread( new Runnable() {
                @Override
                public void run() {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( googleApiClient ).await();
                    for(Node node : nodes.getNodes()) {
                        Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), PATH, null).await();
                    }
                }
            }).start();
        }
    }*/

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = (float) 0.1;

            float[] gravity = new float[3];
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            this.accXwoG = event.values[0] - gravity[0];
            this.accYwoG = event.values[1] - gravity[1];
            this.accZwoG = event.values[2] - gravity[2];

            // RMS Value
            rmsValue = sqrt(pow(this.accXwoG,2) + pow(this.accYwoG,2) + pow(this.accZwoG,2));
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

            UpdateArray myUpdateArrayTask = new UpdateArray((float) rmsValue, df.format(currentDate));
            myHandler_.post(myUpdateArrayTask);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.startSleep:
                sensorManager_.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
                Toast.makeText(getApplicationContext(), "Starting", Toast.LENGTH_LONG).show();
                startButton.setClickable(false);
                // sensorManager_.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
//                Intent intent = new Intent(WearConnection.this, StopSleep.class);
//                startActivity(intent);
                break;
        }
    }

    /*@Override
    public void onConnected(@Nullable Bundle bundle) {
        final PutDataMapRequest putRequest = PutDataMapRequest.create("/my_path");
        final DataMap map = putRequest.getDataMap();
        int startIndex = 1;

        for(int index = 0; index < newRMSMilliValues.size(); index++){
            List<Object> temp = newRMSMilliValues.get(index);
            String str = temp.get(0) + "," + temp.get(1);
            map.putInt(str, startIndex);
            startIndex++;
        }

        Wearable.DataApi.putDataItem(googleApiClient, putRequest.asPutDataRequest());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/

    /*@Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        String[] stringRMSData = new String[newRMSMilliValues.size()];
        int startIndex = 0;
        List<Object> temp = new ArrayList<>();

        for(int index = 0; index < stringRMSData.length; index++){
            temp = newRMSMilliValues.get(index);
            String str = temp.get(0) + "," + temp.get(1);
            stringRMSData[startIndex] = str;
            startIndex++;
        }
        new DataTask (getApplicationContext(), stringRMSData).execute();
    }

    class DataTask  extends AsyncTask<Node, Void, Void> {

        private final String[] contents;
        // private MyListener myListener;
        Context c;

        public DataTask (Context c, String [] contents) {
            this.c = c;
            this.contents = contents;
            // this.myListener = myListener;
        }

        @Override
        protected Void doInBackground(Node... nodes) {

            PutDataMapRequest dataMap = PutDataMapRequest.create ("/myapp/myevent");
            dataMap.getDataMap().putStringArray("contents", contents);

            PutDataRequest request = dataMap.asPutDataRequest();

            DataApi.DataItemResult dataItemResult = Wearable.DataApi
                    .putDataItem(googleApiClient, request).await();


            Log.d ("Data sent in background", "/myapp/myevent"+getStatus());
            return null;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }*/

    public class StartStopButton implements  Runnable {
        @Override
        public void run() {
            startButton.setOnClickListener((View.OnClickListener) this);
            Intent intent = new Intent(WearConnection.this, StopSleep.class);
            startActivity(intent);
        }
    }

    public class UpdateArray implements  Runnable{

        private float rms_change;
        private String time_change;

        public UpdateArray(float rms_change_, String time_change_) {
            this.rms_change = rms_change_;
            this.time_change = time_change_;
        }


        @Override
        public void run() {
            List<Object> values = new ArrayList<>();
            values.add(time_change);
            values.add(rms_change);
            newRMSMilliValues.add(values);
            Log.d("Updating the array", "values:" + values);
    }
    }

    public class Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            String onMessageReceived = "Just received a message from handheld";
            mTextView.setText(onMessageReceived);
//            startButton.setClickable(true);
            buttonStatus = true;
        }
    }

    class SendMessage extends Thread {
        String path;
//        String message;
        ArrayList<List<Object>> sendRMSData = new ArrayList<List<Object>>();
        //Constructor for sending information to the Data Layer//
        SendMessage(String p, ArrayList<List<Object>> sendRMSData_) {
            path = p;
//            message = m;
            this.sendRMSData = sendRMSData_;
            Log.d("Inside Constructor", "Test1");
        }

        public void run() {
            //Retrieve the connected devices//
            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                //Block on a task and get the result synchronously//
                List<Node> nodes = Tasks.await(nodeListTask);
                for (Node node : nodes) {
                    //Send the message///
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(sendRMSData);
                    byte[] bytes = bos.toByteArray();
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(WearConnection.this).sendMessage(node.getId(), path, bytes);
                    Log.d("After sending data", "Test3");
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                        //Handle the errors//
                    } catch (ExecutionException exception) {
                        //TO DO//
                    } catch (InterruptedException exception) {
                        //TO DO//
                    }
                }
            } catch (ExecutionException exception) {
                //TO DO//
            } catch (InterruptedException exception) {
                //TO DO//
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
