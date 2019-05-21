package duong.huy.huong.healthcare.SleepRecorder;

import android.content.Context;

public class MyApplication extends android.app.Application {

    public static Context context;
    public static Recorder recorder = new Recorder();

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;
        recorder = new Recorder();
    }
}
