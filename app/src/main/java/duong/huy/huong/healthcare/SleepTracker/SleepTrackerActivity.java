package duong.huy.huong.healthcare.SleepTracker;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import duong.huy.huong.healthcare.R;

public class SleepTrackerActivity extends AppCompatActivity {
    LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracker);
        getSupportActionBar().hide();

        mLineChart = (LineChart) findViewById(R.id.chart2);
        drawChart();
    }
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
        //mLineChart.getXAxis().setEnabled(false);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //mBarData.addEntry(new Entry(dataset));
        //mBarChart.setData(mBarData);
        //mBarChart.setDescription("số bước đi trong 5 ngày gần đây");



    }
}
