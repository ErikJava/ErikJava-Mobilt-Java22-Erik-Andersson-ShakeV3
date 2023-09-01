package com.example.myapplication;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView square;
    private long lastShakeTime;
    private static final int SHAKE_DELAY = 1000;
    private static final long HIDE_DELAY = 2375;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        square = findViewById(R.id.tv_square);

        setUpSensorStuff();
        lastShakeTime = System.currentTimeMillis();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main, ShakeFragment.newInstance(shakeCounter))
                        .commit();
            }
        });
    }

    private boolean sensorListenerRegistered = false;

    private void setUpSensorStuff() {
        if (!sensorListenerRegistered) {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this,
                        accelerometer,
                        SensorManager.SENSOR_DELAY_FASTEST);
                sensorListenerRegistered = true;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float sides = sensorEvent.values[0];
            float upDown = sensorEvent.values[1];

            square.setRotationX(upDown * 3f);
            square.setRotationY(sides * 3f);
            square.setRotation(-sides);
            square.setTranslationX(sides * -10);
            square.setTranslationY(upDown * 10);

            float threshold = 0.3f;
            int color = (Math.abs(upDown) <= threshold && Math.abs(sides) <= threshold)
                    ? Color.GREEN : Color.RED;
            square.setBackgroundColor(color);
            square.setText("up/down " + (int) upDown + "\nleft/right " + (int) sides);
            detectShake(sides, upDown);
        }
    }

    private int shakeCounter = 0;

    private void detectShake(float x, float y) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastShakeTime) > SHAKE_DELAY) {
            double acceleration = Math.sqrt(x * x + y * y);
            if (acceleration > 12) {
                lastShakeTime = currentTime;
                showToast("Device shaken!");
                shakeCounter++;

                ImageView imageView = findViewById(R.id.image);
                imageView.setVisibility(View.VISIBLE);

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setVisibility(View.GONE);
                    }
                }, HIDE_DELAY);
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorListenerRegistered) {
            sensorManager.unregisterListener(this);
            sensorListenerRegistered = false;
        }
    }
}


