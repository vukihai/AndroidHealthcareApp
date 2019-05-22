package duong.huy.huong.healthcare.SleepRecorder;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class Recorder {
    private static String TAG = "SleepMinderRecorder";
    private AudioRecorder audioRecorder = null;
    private long startTime = 0;
    private PowerManager.WakeLock wakeLock;
    private boolean running = false;
    private NoiseModel noiseModel = new NoiseModel();
    private String status = "";
    private int phase;
    private int sleep, wake, noise;
    public static Context context;
    Intent intent;

    @SuppressLint("InvalidWakeLockTag")
    public void start(Context context) {
        this.running = true;
        PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SleepMinderLock");
        wakeLock.acquire();
        this.startTime = System.currentTimeMillis();
        phase = 0;
        sleep = 0;
        wake = 0;
        noise = 0;
        audioRecorder = new AudioRecorder(noiseModel);
        audioRecorder.start();
        final android.os.Handler customHandler = new android.os.Handler();
        Runnable updateTimerThread = new Runnable()
        {
            public void run() {
                synchronized (Recorder.this) {
                    if(audioRecorder == null) {
                        return;
                    }
                    if(noiseModel.getMovement() > 1) {
                        wake++;
                    }
                    else if(noiseModel.getSnore() > 5) {
                        sleep++;
                    }
                    if(noiseModel.getEvent() == 0  ){
                        noise++;
                    }
                    dumpData();
                    noiseModel.resetEvents();
                    customHandler.postDelayed(this, 5000);
                }
            }
        };

        customHandler.postDelayed(updateTimerThread, 0);
    }

    /**
     * ngừng theo dõi.
     */
    public void stop(Context context) {
        synchronized (this) {
            if(audioRecorder != null) {
                audioRecorder.close();
                audioRecorder = null;
            }

            wakeLock.release();

            dumpData();

            // clean
            startTime = System.currentTimeMillis();
            phase = 0;
            sleep = 0;
            wake = 0;
            noise = 0;
            running = false;
            status = "";
        }
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Outputs the accumulated data
     */
    private void dumpData() {
        if(intent == null) intent = new Intent("duong.huy.huong.sleepbroadcast");
        intent.putExtra("startTime", String.valueOf(startTime));
        intent.putExtra("phase", String.valueOf(phase));
        intent.putExtra("sleep", String.valueOf(sleep));
        intent.putExtra("wake", String.valueOf(wake));
        intent.putExtra("noise", String.valueOf(noise));
        intent.putExtra("status", status);
        context.sendBroadcast(intent);
        Log.d("sleep recorder", "status" + status);
        Log.d("sleep recorder", "chi so " + sleep + wake);
        if(System.currentTimeMillis() - startTime > phase*300000 ) {
            phase++;
            if(sleep>wake) status += "0 ";
            else status += "1 ";
            sleep = 0;
            wake = 0;
            noise = 0;
        }
    }
}
