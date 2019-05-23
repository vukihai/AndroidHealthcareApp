package duong.huy.huong.healthcare.SleepRecorder;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import duong.huy.huong.healthcare.MainActivity;
import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.db.Sleeprecorder;
import duong.huy.huong.healthcare.db.SleeprecorderDao;
import duong.huy.huong.healthcare.db.Step;
import duong.huy.huong.healthcare.db.StepDao;

import static android.support.constraint.Constraints.TAG;

public class SleepRecorderActivity extends AppCompatActivity {
    private TextView test = null;
    public static Context context;
    Button runService;
    Intent trackingIntent;
    TextView wake;
    TextView deep;
    TextView snoor;
    TextView noise;
    LineChart mLineChart;
    static final int PICK_HISTORY = 1;
    int curPhase = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_recorder);
        this.getSupportActionBar().hide();
        wake = (TextView) findViewById(R.id.wake);
        deep = (TextView) findViewById(R.id.deep);
        snoor = (TextView) findViewById(R.id.snoor);
        noise = (TextView) findViewById(R.id.noise);
        mLineChart = (LineChart) findViewById(R.id.chart2);

        trackingIntent = new Intent(SleepRecorderActivity.this, RecordingService.class);
        runService = (Button) findViewById(R.id.button3);
        if(!isMyServiceRunning(RecordingService.class)) {
            runService.setText("chạy theo dõi");
        } else {
            runService.setText("dừng theo dõi");
        }
        registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.sleepbroadcast"));


        Sleeprecorder defaul = new Sleeprecorder();
        defaul.settime(String.valueOf(System.currentTimeMillis()));
        defaul.setstatus("0");
        drawChart(defaul);
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
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
    public void runRecorder(View v) {
        if(!isMyServiceRunning(RecordingService.class)) {

            startService(trackingIntent);
            Snackbar
                    .make(findViewById(R.id.main_layout), "theo dõi đang hoạt động. good night!", Snackbar.LENGTH_LONG)
                    .show();
            runService.setText("dừng theo dõi");
        } else {
            stopService(trackingIntent);
            runService.setText("chạy theo dõi");
            deleteOldRecord();
            ArrayList<Sleeprecorder> mSleeprecorders =SleeprecorderDao.loadAllRecords();
            if(mSleeprecorders != null && mSleeprecorders.size()>0) {
                drawChart(mSleeprecorders.get(mSleeprecorders.size()-1));
            }
        }
    }
    public void viewHistory(View v){
        Intent his = new Intent(this, SleepHistoryActivity.class);
        startActivityForResult(his, PICK_HISTORY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_HISTORY) {
            if (resultCode == RESULT_OK) {
                String res = data.getStringExtra("result");
                drawChart(SleeprecorderDao.loadAllRecords().get(Integer.valueOf(res)));
            }
        }
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int sleep = Integer.valueOf(intent.getStringExtra("sleep"));
            int wakes = Integer.valueOf(intent.getStringExtra("wake"));
            int noises = Integer.valueOf(intent.getStringExtra("noise"));
            if(sleep>(wakes*6/5) && sleep > 20){
                deep.setText("Ngủ sâu: " + String.valueOf(sleep-(wakes*6/5)));
            } else {
                deep.setText("Ngủ sâu: 0");
            }
            snoor.setText("Ngủ nông: "+ String.valueOf(sleep));
            wake.setText("Thức giấc: " + String.valueOf(wakes));
            noise.setText("Nhiễu: " + String.valueOf(noises));
            TextView pha = (TextView) findViewById(R.id.textView15);
            pha.setText("phase: " + Integer.valueOf(intent.getStringExtra("phase")));
        }
    };
    private void deleteOldRecord(){
        ArrayList<Sleeprecorder> sleeprecorders = SleeprecorderDao.loadAllRecords();
        if(sleeprecorders != null && sleeprecorders.size() != 0) {
            while (sleeprecorders.size() > 20) {
                SleeprecorderDao.deleteRecord(sleeprecorders.get(0));
                sleeprecorders.remove(0);
            }
        }
    }
    private void drawChart(Sleeprecorder theLast){
        ArrayList<Entry> entries = new ArrayList<>();
        String [] phase = theLast.getstatus().split(" ");
        if(theLast.getstatus()==" ") return;
        if(phase.length == 1 && Integer.valueOf(phase[0])==0) return;
        int wakeCount = 1;
        int deepCount = 0;
        int snoorCount = 0;
        for(int i=0; i<phase.length; i++) {
            entries.add(new Entry(i, Float.valueOf(phase[i])));
            if(Integer.valueOf(phase[i]) == 0) wakeCount++;
            if(Integer.valueOf(phase[i]) == 1) snoorCount++;
            if(Integer.valueOf(phase[i]) == 2) deepCount++;
        }
        LineDataSet dataset = new LineDataSet(entries, "phase");
        dataset.setDrawIcons(false);
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawFilled(true);
        dataset.setFillAlpha(255);
        dataset.setFillColor(ContextCompat.getColor(getBaseContext(), R.color.step_chart));
        dataset.setColor(ContextCompat.getColor(getBaseContext(), R.color.step_chart));
        dataset.setDrawCircles(false);
        LineData mLineData = new LineData(dataset);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getDescription().setText("");
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getXAxis().setEnabled(false);


        TextView wakePercent = (TextView) findViewById(R.id.wake_percent);
        wakePercent.setText(String.valueOf(Math.round(wakeCount*100/(wakeCount+snoorCount+deepCount))) + "%");
        TextView snoorPercent = (TextView) findViewById(R.id.snoor_percent);
        snoorPercent.setText(String.valueOf(Math.round(snoorCount*100/(wakeCount+snoorCount+deepCount))) + "%");
        TextView deepPercent = (TextView) findViewById(R.id.deep_percent);
        deepPercent.setText(String.valueOf(Math.round(deepCount*100/(wakeCount+snoorCount+deepCount))) + "%");
        TextView quatity = (TextView) findViewById(R.id.quatity);
        quatity.setText(String.valueOf(Math.round((deepCount*40+snoorCount*60)/(wakeCount+snoorCount+deepCount))) + "%");

        TextView sleepTime = (TextView) findViewById(R.id.sleep_time);


        Timestamp stamp = new Timestamp(Long.valueOf(theLast.gettime()));
        Date date = new Date(stamp.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String strDate = formatter.format(date);

        sleepTime.setText(strDate);
        TextView snoorTime = (TextView) findViewById(R.id.snoor_time);
        snoorTime.setText(String.valueOf(Math.round((snoorCount)*5/60)) + "G" + String.valueOf(((snoorCount)*5)%60)+"P");
        TextView deepTime = (TextView) findViewById(R.id.deep_time);
        deepTime.setText(String.valueOf(Math.round((deepCount)*5/60)) + "G" + String.valueOf(((deepCount)*5)%60)+"P");
        TextView total = (TextView) findViewById(R.id.total);
        total.setText(String.valueOf(Math.round((wakeCount+snoorCount+deepCount)*5/60)) + "G" + String.valueOf(((wakeCount+snoorCount+deepCount)*5)%60)+"P");
    }
}
