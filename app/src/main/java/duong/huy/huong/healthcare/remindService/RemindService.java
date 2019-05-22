package duong.huy.huong.healthcare.remindService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.db.User_Info;
import duong.huy.huong.healthcare.db.User_InfoDao;

public class RemindService extends Service {
    private boolean motionReminder = false;
    private boolean sleepReminder = false;
    private int curStep = 0;
    private long lastStepTime = 0;
    public RemindService() {
        registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.stepcounterbroadcast"));
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!motionReminder) return;
            if(lastStepTime == 0) {
                lastStepTime = System.currentTimeMillis();
                return;
            }
            Log.d("nhac van dong:", String.valueOf(System.currentTimeMillis() - lastStepTime));
            int newStep = Integer.valueOf(intent.getStringExtra("numSteps"));
            if(newStep !=curStep) {
                curStep = newStep;
                lastStepTime = System.currentTimeMillis();
            } else {
                if(System.currentTimeMillis()-lastStepTime > 60000) {
                    Log.d("asdasdad", " nhac van dong");
                }
            }


        }

    };
    public void setMotionReminder(boolean motionReminder) {
        this.motionReminder = motionReminder;
    }

    public void setSleepReminder(boolean sleepReminder) {
        this.sleepReminder = sleepReminder;
    }
    public boolean getMotionReminder(){
        return motionReminder;
    }
    public boolean getSleepReminder() {
        return sleepReminder;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
