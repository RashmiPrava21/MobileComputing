package com.example.loginpage;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;

public class GyroData extends AppCompatActivity
        implements View.OnClickListener, SensorEventListener {

    private Handler myHandler_;
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText sensorGyroX, sensorGyroY, sensorGyroZ;

    private SensorManager sensorManager_;
    private Sensor gyroscope;
    // private boolean datapresentGyro = false;

    private Float gyroX = (float) 0.0;
    private Float gyroY = (float) 0.0;
    private Float gyroZ = (float) 0.0;

    private String currentDateTimeString; // = DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_data);

        myHandler_ = new Handler();

        // Gyroscope
        sensorGyroX = findViewById(R.id.sensorGyroX);
        sensorGyroY = findViewById(R.id.sensorGyroY);
        sensorGyroZ = findViewById(R.id.sensorGyroZ);

        currentDateTimeString = findViewById(R.id.timeStamp);

        // Button
        Button convertButton = findViewById(R.id.CollectData);
        convertButton.setOnClickListener(this);

        sensorManager_ = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager_.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (gyroscope == null) {
            Log.i(TAG, "Gyroscope not supported");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (event.values[0] != 0.0 || event.values[1] != 0.0 || event.values[2] != 0.0) {
                this.gyroX = event.values[0];
                this.gyroY = event.values[1];
                this.gyroZ = event.values[2];
            }

            // datapresentGyro = true;
            GyroTask myTask = new GyroTask(gyroX, gyroY, gyroZ);
            myHandler_.post(myTask);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.CollectData:
                sensorManager_.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                break;
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
}
