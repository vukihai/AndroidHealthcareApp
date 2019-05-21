package duong.huy.huong.healthcare.SleepRecorder;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import duong.huy.huong.healthcare.R;

import static android.support.constraint.Constraints.TAG;


public class RecordingService extends Service {

    private final int ONGOING_NOTIFICATION_ID = 1;
    public static RecordingService instance;

    public RecordingService() {
        instance = this;
    }

    @Override
    public void onDestroy() {
        MyApplication.recorder.stop(MyApplication.context);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyApplication myApplication = new MyApplication();
        Log.d(TAG, "service ran");
        startForeground(ONGOING_NOTIFICATION_ID, getNotification());
        myApplication.recorder.context = this;
        myApplication.recorder.start(this);
        return START_STICKY;
    }
    private Notification getNotification() {
        Log.d(TAG, "getNotification: runable");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "M_CH_ID")
                        .setSmallIcon(R.drawable.green_icon)
                        .setContentTitle("SleepMinder is active")
                        .setContentText("Sleep well :)");
        Intent resultIntent = new Intent(this, SleepRecorderActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SleepRecorderActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
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
}