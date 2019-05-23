package duong.huy.huong.healthcare.remindService;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import androidx.annotation.RequiresApi;
import duong.huy.huong.healthcare.MainActivity;
import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.StepCounter.StepCounterSrv;
@SuppressWarnings("ALL")
public class MotionRemindService extends Service {
    private int curStep = 0;
    private long lastStepTime = 0;
    Intent stepSrv = null;
    public MotionRemindService() {
    }
    @Override
    public void onCreate(){
        super.onCreate();
        if(!isMyServiceRunning(StepCounterSrv.class)){
            stepSrv = new Intent(getBaseContext(), StepCounterSrv.class);
            startService(stepSrv);
        }
        registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.stepcounterbroadcast"));
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onDestroy(){
        unregisterReceiver(broadcastReceiver);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(lastStepTime == 0) {
                lastStepTime = System.currentTimeMillis();
                return;
            }
            int newStep = Integer.valueOf(intent.getStringExtra("numSteps"));
            if(newStep !=curStep) {
                curStep = newStep;
                lastStepTime = System.currentTimeMillis();
            } else {
                if(System.currentTimeMillis()-lastStepTime > 10000) {
                    Log.d("asdasdad", " nhac van dong");
                    showNoti();
                    lastStepTime = System.currentTimeMillis();
                }
            }


        }

    };
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNoti() {
        Integer mId = 0;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.splash_icon)
                        .setContentTitle("Ngồi quá lâu!")
                        .setContentText("bạn nên đi lại 1 lát.");
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        Notification note = mBuilder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, note);

    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
