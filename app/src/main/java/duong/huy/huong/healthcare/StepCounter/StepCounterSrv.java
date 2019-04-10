package duong.huy.huong.healthcare.StepCounter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import duong.huy.huong.healthcare.db.DbManager;
import duong.huy.huong.healthcare.db.Step;
import duong.huy.huong.healthcare.db.StepDao;

public class StepCounterSrv extends Service implements SensorEventListener, StepListener {
    SensorManager sensorManager;
    Sensor accel; // cam bien gia toc
    int numSteps; // so buoc di
    boolean isStopped;
    Intent intent;
    Step mStep;
    StepDao mStepDao;
    StepDetector simpleStepDetector;
    Date startDate;
    private final Handler handler = new Handler();
    public StepCounterSrv() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        DbManager.setConfig(this);
        startDate = Calendar.getInstance().getTime();
        intent = new Intent("duong.huy.huong.stepcounterbroadcast");
        this.isStopped = false;
    }
    @Override
    public void onDestroy() {
        this.isStopped = true;
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
        handler.removeCallbacks(updateDB);
        handler.post(updateDB);
        return START_STICKY;
    }
    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!isStopped) { // Only allow the repeating timer while service is running (once service is stopped the flag state will change and the code inside the conditional statement here will not execute).
                // Call the method that broadcasts the data to the Activity..
                broadcastSensorValue();
                // Call "handler.postDelayed" again, after a specified delay.
                if(!isStopped)
                    handler.postDelayed(this, 1000);
            }
        }
    };
    private Runnable updateDB = new Runnable() {
        public void run() {
            if (!isStopped) {
                mStep = StepDao.loadRecordByStep_Date(String.valueOf(Calendar.getInstance().getTime().getTime()/1000/60/24));
                if(mStep == null) {
                    mStep = new Step();
                    mStep.setstep(String.valueOf(numSteps));
                    mStep.setstep_date(String.valueOf(Calendar.getInstance().getTime().getTime()/1000/60/24));
                    StepDao.insertRecord(mStep);
                } else {
                    mStep.setstep(String.valueOf(numSteps));
                    StepDao.updateRecord(mStep);
                }
                handler.postDelayed(this, 10000);
            }
        }
    };

    private void broadcastSensorValue() {
        intent.putExtra("numSteps", String.valueOf(numSteps));
        intent.putExtra("time", String.valueOf(Calendar.getInstance().getTime().getTime() - startDate.getTime()));
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
