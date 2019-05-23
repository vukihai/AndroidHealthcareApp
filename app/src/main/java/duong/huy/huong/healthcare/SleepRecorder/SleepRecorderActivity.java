package duong.huy.huong.healthcare.SleepRecorder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import duong.huy.huong.healthcare.MainActivity;
import duong.huy.huong.healthcare.R;

import static android.support.constraint.Constraints.TAG;

public class SleepRecorderActivity extends AppCompatActivity {
    private TextView test = null;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_recorder);
        //test = findViewById(R.id.textView9);

        Intent trackingIntent = new Intent(SleepRecorderActivity.this, RecordingService.class);
        startService(trackingIntent);
        Snackbar
                .make(findViewById(R.id.main_layout), "theo dõi đang hoạt động. good night!", Snackbar.LENGTH_LONG)
                .show();
    }
}
