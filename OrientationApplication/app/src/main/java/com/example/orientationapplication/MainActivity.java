package com.example.orientationapplication;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.TimeZone;

import static java.lang.Math.*;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SensorEventListener, Serializable {

    private Handler myHandler_;
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText sensorX, sensorY, sensorZ;
    private EditText sensorGyroX, sensorGyroY, sensorGyroZ;
    private EditText sensorXwithoutG, sensorYwithoutG, sensorZwithoutG;
    private EditText rms;

    private SensorManager sensorManager_;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private EditText azimuth;
    private EditText pitch;
    private EditText roll;

    // final orientation angles from sensor fusion
    private float[] fusedOrientation = new float[3];
    private boolean datapresentAcc = false;
    private boolean datapresentGyro = false;
    public float filter_coefficient = 0.90f;
    private Float accX = (float) 0.0;
    private Float accY = (float) 0.0;
    private Float accZ = (float) 0.0;
    private Float gyroX = (float) 0.0;
    private Float gyroY = (float) 0.0;
    private Float gyroZ = (float) 0.0;

    private double rmsValue = 0.0f;
    private long currentDateTime;
    private Date currentDate;

    private Float accXwoG = (float) 0.0;
    private Float accYwoG = (float) 0.0;
    private Float accZwoG = (float) 0.0;
    private float[] accelerometerData = new float[3];

    Formatter fmt = new Formatter();
    ArrayList<List<Object>> newRMSMilliValues = new ArrayList<List<Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHandler_ = new Handler();

        // Accelerometer
        /*sensorX = findViewById(R.id.sensorX);
        sensorY = findViewById(R.id.sensorY);
        sensorZ = findViewById(R.id.sensorZ);
*/
        // Gyroscope
        sensorGyroX = findViewById(R.id.sensorGyroX);
        sensorGyroY = findViewById(R.id.sensorGyroY);
        sensorGyroZ = findViewById(R.id.sensorGyroZ);

        // Accelerometer without Gravity
        sensorXwithoutG = findViewById(R.id.xwoG);
        sensorYwithoutG = findViewById(R.id.ywoG);
        sensorZwithoutG = findViewById(R.id.zwoG);

        // RMS Value
        rms = findViewById(R.id.rmsValue);

        // Azimuth
        azimuth =  findViewById(R.id.azimuth);
        // Pitch
        pitch = findViewById(R.id.pitch);
        // Roll
        roll = findViewById(R.id.roll);

        // Button
        Button convertButton = findViewById(R.id.CollectData);
        convertButton.setOnClickListener(this);

        // Stop Collecting Button
        Button stopButton = findViewById(R.id.StopButton);
        stopButton.setOnClickListener(this);

        sensorManager_ = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager_.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager_.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (accelerometer == null) {
            Log.i(TAG, "Accelerometer not supported");
        }

        if (gyroscope == null) {
            Log.i(TAG, "Gyroscope not supported");
        }


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("retrieveValues", (Serializable) newRMSMilliValues);
                Intent intent = new Intent(MainActivity.this, RMSValuesDisplay.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.CollectData:
                sensorManager_.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
                sensorManager_.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                break;
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
        }
    }
    public class RMVValueTask implements Runnable {

        private float rms_value;
        public RMVValueTask(float rms_value_) {
            this.rms_value = rms_value_;
        }
        @Override
        public void run() {
            Formatter fmt = new Formatter();
//            fmt.format("%.4f", rms_value);

            rms.setText(fmt.format("%.4f", rms_value).toString());
        }
    }

    public class AccwoGTask implements  Runnable {

        private float accxwoG, accywoG, acczwoG;
        public AccwoGTask(float accxwoG_, float accywoG_, float acczwoG_) {
            this.accxwoG = accxwoG_;
            this.accywoG = accywoG_;
            this.acczwoG = acczwoG_;
        }
        @Override
        public void run() {
            sensorXwithoutG.setText(new Float(accxwoG).toString());
            sensorYwithoutG.setText(new Float(accywoG).toString());
            sensorZwithoutG.setText(new Float(acczwoG).toString());
        }
    }

    public class AccTask implements Runnable {

        private float accX, accY, accZ;
        public AccTask(float accX_, float accY_, float accZ_) {
            this.accX = accX_;
            this.accY = accY_;
            this.accZ = accZ_;
        }
        @Override
        public void run() {
            sensorX.setText(new Float(accX).toString());
            sensorY.setText(new Float(accY).toString());
            sensorZ.setText(new Float(accZ).toString());
        }
    }

    public class GyroTask implements Runnable {

        private float gyroX, gyroY, gyroZ;
        public GyroTask(float gyroX_, float gyroY_, float gyroZ_) {
            this.gyroX = gyroX_;
            this.gyroY = gyroY_;
            this.gyroZ = gyroZ_;
        }
        @Override
        public void run() {
            sensorGyroX.setText(new Float(gyroX).toString());
            sensorGyroY.setText(new Float(gyroY).toString());
            sensorGyroZ.setText(new Float(gyroZ).toString());
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            this.accX = event.values[0];
            this.accY = event.values[1];
            this.accZ = event.values[2];

//            AccTask myTask = new AccTask(accX, accY, accZ);
//            myHandler_.post(myTask);

            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate

            final float alpha = (float) 0.1;

            float[] gravity = new float[3];
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            this.accXwoG = event.values[0] - gravity[0];
            this.accYwoG = event.values[1] - gravity[1];
            this.accZwoG = event.values[2] - gravity[2];
            datapresentAcc = true;

            AccwoGTask myTaskwithoutG = new AccwoGTask(accXwoG, accYwoG, accZwoG);
            myHandler_.post(myTaskwithoutG);

            // RMS Value
            rmsValue = sqrt(pow(this.accXwoG,2) + pow(this.accYwoG,2) + pow(this.accZwoG,2));
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");





            UpdateArray myUpdateArrayTask = new UpdateArray((float) rmsValue, df.format(currentDate));
            myHandler_.post(myUpdateArrayTask);

            RMVValueTask myRMSTask = new RMVValueTask((float) rmsValue);
            myHandler_.post(myRMSTask);
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (event.values[0] != 0.0 || event.values[1] != 0.0 || event.values[2] != 0.0) {
                this.gyroX = event.values[0];
                this.gyroY = event.values[1];
                this.gyroZ = event.values[2];
            }

            /*this.gyroY = event.values[1];
            this.gyroZ = event.values[2];*/
            datapresentGyro = true;
            GyroTask myTask = new GyroTask(gyroX, gyroY, gyroZ);
            myHandler_.post(myTask);
        }

        if (datapresentGyro && datapresentAcc) {
            float oneMinusCoeff = 1.0f - filter_coefficient;
            float acclX = this.accX;
            float acclY = this.accY;
            float acclZ = this.accZ;
            float gyroX = this.gyroX;
            float gyroY = this.gyroY;
            float gyroZ = this.gyroZ;

            // Azimuth
            if (gyroX < -0.5 * Math.PI && acclX > 0.0) {
                fusedOrientation[0] = (float) (filter_coefficient * (gyroX + 2.0 * Math.PI) + oneMinusCoeff * acclX);
                fusedOrientation[0] -= (fusedOrientation[0] > Math.PI) ? 2.0 * Math.PI : 0;
                azimuth.setText(new Float(fusedOrientation[0]).toString());
            } else if (acclX < -0.5 * Math.PI && gyroX > 0.0) {
                fusedOrientation[0] = (float) (filter_coefficient * gyroX + oneMinusCoeff * (acclX + 2.0 * Math.PI));
                fusedOrientation[0] -= (fusedOrientation[0] > Math.PI) ? 2.0 * Math.PI : 0;
                azimuth.setText(new Float(fusedOrientation[0]).toString());
            } else
                fusedOrientation[0] = filter_coefficient * gyroX + oneMinusCoeff * acclX;
            azimuth.setText(new Float(fusedOrientation[0]).toString());

            // Pitch
            if (gyroY < -0.5 * Math.PI && acclY > 0.0) {
                fusedOrientation[1] = (float) (filter_coefficient * (gyroY + 2.0 * Math.PI) + oneMinusCoeff * acclY);
                fusedOrientation[1] -= (fusedOrientation[1] > Math.PI) ? 2.0 * Math.PI : 0;
                pitch.setText(new Float(fusedOrientation[1]).toString());
            } else if (acclY < -0.5 * Math.PI && gyroY > 0.0) {
                fusedOrientation[1] = (float) (filter_coefficient * gyroY + oneMinusCoeff * (acclY + 2.0 * Math.PI));
                fusedOrientation[1] -= (fusedOrientation[1] > Math.PI) ? 2.0 * Math.PI : 0;
                pitch.setText(new Float(fusedOrientation[1]).toString());
            } else
                fusedOrientation[1] = filter_coefficient * gyroY + oneMinusCoeff * acclY;
            pitch.setText(new Float(fusedOrientation[1]).toString());

            // Roll
            if (gyroZ < -0.5 * Math.PI && acclZ > 0.0) {
                fusedOrientation[2] = (float) (filter_coefficient * (gyroZ + 2.0 * Math.PI) + oneMinusCoeff * acclZ);
                fusedOrientation[2] -= (fusedOrientation[2] > Math.PI) ? 2.0 * Math.PI : 0;
                roll.setText(new Float(fusedOrientation[2]).toString());
            } else if (acclZ < -0.5 * Math.PI && gyroZ > 0.0) {
                fusedOrientation[2] = (float) (filter_coefficient * gyroZ + oneMinusCoeff * (acclZ + 2.0 * Math.PI));
                fusedOrientation[2] -= (fusedOrientation[2] > Math.PI) ? 2.0 * Math.PI : 0;
                roll.setText(new Float(fusedOrientation[2]).toString());
            } else
                fusedOrientation[2] = filter_coefficient * gyroZ + oneMinusCoeff * acclZ;
            roll.setText(new Float(fusedOrientation[2]).toString());
        datapresentGyro = false;
        datapresentAcc = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
