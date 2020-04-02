package com.example.bolasepak;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.bolasepak.App.CHANNEL_ID;

public class StepService extends Service implements SensorEventListener{

    SensorManager sensorManager;
    String steps;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "onCreate() StepService");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "onStartCommand() StepService");


        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepSensor != null){
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("BolaSepak")
                .setContentText("Steps are measured")
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        Log.d("Service", "onStartCommand() return");


        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        steps = String.valueOf(event.values[0]);
        sendMessageToActivity(steps);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendMessageToActivity(String msg){
        Intent intent = new Intent("StepUpdate");
        intent.putExtra("Step", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
