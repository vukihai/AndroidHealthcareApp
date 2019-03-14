package duong.huy.huong.healthcare.StepCounter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

public class StepCounterSrv extends Service implements SensorEventListener, StepListener {
    SensorManager sensorManager;
    Sensor accel; // cam bien gia toc
    int numSteps; // so buoc di
    boolean isStopped;
    Intent intent;
    StepDetector simpleStepDetector;
    private final Handler handler = new Handler();
    public StepCounterSrv() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent("duong.huy.huong.stepcounterbroadcast");
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        numSteps = 0;
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        isStopped = false;

        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData); // 0 seconds
        return START_STICKY;
    }
    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!isStopped) { // Only allow the repeating timer while service is running (once service is stopped the flag state will change and the code inside the conditional statement here will not execute).
                // Call the method that broadcasts the data to the Activity..
                broadcastSensorValue();
                // Call "handler.postDelayed" again, after a specified delay.
                handler.postDelayed(this, 1000);
            }
        }
    };
    private void broadcastSensorValue() {
        //Log.d(TAG, "Data to Activity");
        // add step counter to intent.
        intent.putExtra("numSteps", String.valueOf(numSteps));
//        intent.putExtra("Counted_Step", String.valueOf(newStepCounter));
//        // add step detector to intent.
//        intent.putExtra("Detected_Step_Int", currentStepsDetected);
//        intent.putExtra("Detected_Step", String.valueOf(currentStepsDetected));
//        // call sendBroadcast with that intent  - which sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }


    public void step(long timeNs) {
        numSteps++;
        //TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
