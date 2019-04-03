package duong.huy.huong.healthcare.StepCounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import duong.huy.huong.healthcare.R;

public class StepCounterActivity extends AppCompatActivity {
    ImageView headerCircle;
    LineChart mLineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        getSupportActionBar().hide();

        headerCircle = (ImageView) findViewById(R.id.circle_step);
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(14000);
        rotate.setRepeatCount(Animation.INFINITE);
        headerCircle.setAnimation(rotate);
        registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.stepcounterbroadcast"));

        mLineChart = (LineChart) findViewById(R.id.chart1);
        drawChart();
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // call updateUI passing in our intent which is holding the data to display.
            TextView t = (TextView) findViewById(R.id.num_step);
            t.setText(String.valueOf(intent.getStringExtra("numSteps")) + " Bước");
        }

    };
    private void drawChart(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 400f));
        entries.add(new Entry(1, 402f));
        entries.add(new Entry(2, 300f));
        entries.add(new Entry(3, 500f));
        entries.add(new Entry(4, 404f));
        entries.add(new Entry(5, 424f));
        entries.add(new Entry(6, 444f));
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
