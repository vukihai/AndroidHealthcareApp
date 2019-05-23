package duong.huy.huong.healthcare.SleepRecorder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.ArrayList;
import java.util.Collections;

import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.db.Heart_Rate;
import duong.huy.huong.healthcare.db.Heart_RateDao;
import duong.huy.huong.healthcare.db.Sleeprecorder;
import duong.huy.huong.healthcare.db.SleeprecorderDao;
import duong.huy.huong.healthcare.db.Walking;
import duong.huy.huong.healthcare.db.WalkingDao;

public class SleepHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_history);

        ListView lv = (ListView) findViewById(R.id.sleepHistory);
        ArrayList<Sleeprecorder> mSleeprecorders = SleeprecorderDao.loadAllRecords();
        Collections.reverse(mSleeprecorders);
        ArrayAdapter<Sleeprecorder> arrayAdapter
                = new ArrayAdapter<Sleeprecorder>(this, android.R.layout.simple_list_item_1 , (Sleeprecorder[]) mSleeprecorders.toArray(new Sleeprecorder[mSleeprecorders.size()]));
        lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ArrayList<Sleeprecorder> mSleeprecorders = SleeprecorderDao.loadAllRecords();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",String.valueOf(mSleeprecorders.size()-(int)id-1));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

}
