package duong.huy.huong.healthcare.StepCounter;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.SleepRecorder.SleepRecorderActivity;
import duong.huy.huong.healthcare.db.DbManager;
import duong.huy.huong.healthcare.db.Step;
import duong.huy.huong.healthcare.db.StepDao;

import static android.support.constraint.Constraints.TAG;

public class StepCounterSrv extends Service implements SensorEventListener, StepListener {
    SensorManager sensorManager;
    Sensor accel; // cam bien gia toc
    int numSteps; // so buoc di
    boolean isStopped;
    Intent intent;
    Step mStep;
    int stepOnDB = 0;
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
    private Notification getNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "M_CH_ID")
                        .setSmallIcon(R.drawable.splash)
                        .setContentTitle("đếm bước chân đang hoạt động");
        Intent resultIntent = new Intent(this, SleepRecorderActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SleepRecorderActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.setProgress(0,0,true);
        mBuilder.setCategory(NotificationCompat.CATEGORY_PROGRESS);
        mBuilder.setOngoing(true);
        return mBuilder.build();
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
        startForeground(1, getNotification());
        return START_STICKY;
    }
    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!isStopped) {
                broadcastSensorValue();
                handler.postDelayed(this, 1000);
            }
        }
    };
    private Runnable updateDB = new Runnable() {
        public void run() {
            if (!isStopped) {
                String day = (String) DateFormat.format("dd",   Calendar.getInstance().getTime());
                mStep = StepDao.loadRecordByStep_Date(day);
                if(mStep == null) {
                    mStep = new Step();
                    mStep.setstep(String.valueOf(numSteps));
                    mStep.setstep_date(day);
                    StepDao.insertRecord(mStep);
                } else {
                    if(stepOnDB == 0) stepOnDB = Integer.valueOf(mStep.getstep());
                    if(numSteps<stepOnDB)
                        mStep.setstep(String.valueOf(numSteps+stepOnDB));
                    else
                        mStep.setstep(String.valueOf(numSteps));
                    StepDao.updateRecord(mStep);
                }
                handler.postDelayed(this, 1000);
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
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
