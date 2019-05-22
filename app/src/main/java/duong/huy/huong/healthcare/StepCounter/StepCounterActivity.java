package duong.huy.huong.healthcare.StepCounter;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.db.DbManager;
import duong.huy.huong.healthcare.db.Step;
import duong.huy.huong.healthcare.db.StepDao;
import duong.huy.huong.healthcare.db.Step_Goal;
import duong.huy.huong.healthcare.db.Step_GoalDao;
import duong.huy.huong.healthcare.db.User_Info;
import duong.huy.huong.healthcare.db.User_InfoDao;

public class StepCounterActivity extends AppCompatActivity {
    ImageView headerCircle;
    LineChart mLineChart;
    Button srvButton;
    Date startDate;
    Intent srv;
    String step_goal;
    TextView goal;
    TextView distance;

    /**
     * Hàm onclick của button đặt mục tiêu.
     * @param v
     */
    public void setStepGoal(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("đặt mục tiêu");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.set_step_goal, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                step_goal = input.getText().toString();
                Step_Goal mStep_goal = new Step_Goal();
                mStep_goal.setstep_goal(String.valueOf(step_goal));
                mStep_goal.setstep_goal_date(String.valueOf(Calendar.getInstance().getTime().getTime()));
                Step_GoalDao.insertRecord(mStep_goal);
                goal.setText("mục tiêu hôm nay: " + step_goal + " bước");
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**
     * Hàm onClick của nút chạy/dừng dịch vụ.
     * @param v
     */
    public void startSrv(View v) {
        if(!isMyServiceRunning(StepCounterSrv.class)) {
            // srv chua chay
            //Toast.makeText(this, "start srv", Toast.LENGTH_LONG).show();
            startService(srv);
            startDate = Calendar.getInstance().getTime();
            srvButton.setText("Tạm dừng");
            Drawable img = getResources().getDrawable( R.drawable.ic_pause);
            srvButton.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);

        } else {
            //Toast.makeText(this, "stop srv", Toast.LENGTH_LONG).show();
            stopService(srv);
            srvButton.setText("Chạy bộ đếm");
            Drawable img = getResources().getDrawable( R.drawable.ic_play_circle_outline_black_24dp);
            srvButton.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            drawChart();
        }
    }

    /**
     * Hàm khởi tạo. init các view và đăng kí nhận broadcast.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbManager.setConfig(this);
        setContentView(R.layout.activity_step_counter);
        getSupportActionBar().hide();

        srv = new Intent(getBaseContext(), StepCounterSrv.class);
        distance = (TextView) findViewById(R.id.distance);
        goal = (TextView) findViewById(R.id.textView);
        ArrayList<Step_Goal> sg = Step_GoalDao.loadAllRecords();
        if(sg.size() >=1) {
            goal.setText("mục tiêu hôm nay: " + String.valueOf(sg.get(sg.size()-1).getstep_goal()) + " bước");
        } else {
            goal.setText("mục tiêu hôm nay: 10.000 bước");
        }

        headerCircle = (ImageView) findViewById(R.id.circle_step);
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(14000);
        rotate.setRepeatCount(Animation.INFINITE);
        headerCircle.setAnimation(rotate);

        registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.stepcounterbroadcast"));
        srvButton = (Button) findViewById(R.id.button1);
        if(!isMyServiceRunning(StepCounterSrv.class)) {
            srvButton.setText("Chạy bộ đếm");
        } else {
            srvButton.setText("Tạm dừng");
        }
        mLineChart = (LineChart) findViewById(R.id.chart1);
        drawChart();
    }

    /**
     * hàm hủy. bỏ đăng kí nhận tin broadcast.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * Hàm kiểm tra xem 1 service có đang chạy không.
     * @param serviceClass
     * @return
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Hàm nhận tin broadcast số bước chân.
     *
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView t = (TextView) findViewById(R.id.num_step);
            t.setText(String.valueOf(intent.getStringExtra("numSteps")) + " Bước");
            TextView t1 = (TextView) findViewById(R.id.textView2);
            t1.setText(String.valueOf((float)Math.round(0.45*Integer.parseInt(intent.getStringExtra("numSteps")))/10) + " calo");
            TextView t2 = (TextView) findViewById(R.id.textVie2);
            String tmp = intent.getStringExtra("time");
            int time = Integer.parseInt(tmp);
            if(time/60000 == 0) {
                t2.setText(String.valueOf(time/1000) + " giây");
            } else {
                t2.setText(String.valueOf(time/60000) + " phút");
            }
            ArrayList<User_Info> mUser_infos = User_InfoDao.loadAllRecords();
            double height = Double.valueOf(mUser_infos.get(0).getheight());
            double dist = Double.valueOf(intent.getStringExtra("numSteps"))*0.415*height/100;
            if(dist < 100) {
                distance.setText(String.valueOf((int)dist) + "m");
            } else {
                distance.setText(String.valueOf((int)(dist/1000)) + "km");
            }


        }

    };

    /**
     * Hàm vẽ biểu đồ lịch sử bước đi. sử dụng thư viện MPChart
     */
    private void drawChart(){
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Step> steps = StepDao.loadAllRecords();
        while(steps.size() >7) {
            StepDao.deleteRecord(steps.get(0));
            steps.remove(0);
        }
        for(int i=0; i<steps.size(); i++) {
            entries.add(new Entry(i, Float.valueOf(steps.get(i).getstep())));
        }
        LineDataSet dataset = new LineDataSet(entries, "Bước");
        dataset.setDrawIcons(false);
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawFilled(true);
        dataset.setFillAlpha(255);
        dataset.setFillColor(ContextCompat.getColor(getBaseContext(), R.color.step_chart));
        dataset.setColor(ContextCompat.getColor(getBaseContext(), R.color.step_chart));
        dataset.setDrawCircles(false);

        //setContentView(mBarChart);
        LineData mLineData = new LineData(dataset);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getDescription().setText("");
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getXAxis().setLabelCount(6);
        mLineChart.getXAxis().setEnabled(false);
        //mBarData.addEntry(new Entry(dataset));
        //mBarChart.setData(mBarData);
        //mBarChart.setDescription("số bước đi trong 5 ngày gần đây");
    }
}
