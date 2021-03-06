package duong.huy.huong.healthcare.HeartRateMonitor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import duong.huy.huong.healthcare.R;
import duong.huy.huong.healthcare.db.Heart_Rate;
import duong.huy.huong.healthcare.db.Heart_RateDao;

public class BeatHistoryActivity extends AppCompatActivity {
    private ListView beatHis = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beat_history);
        beatHis = (ListView) findViewById(R.id.beatHistory);
        ArrayList<Heart_Rate> mHeart_rate = Heart_RateDao.loadAllRecords();
        while(mHeart_rate.size() >20) {
            Heart_RateDao.deleteRecord(mHeart_rate.get(0));
            mHeart_rate.remove(0);
        }
        Collections.reverse(mHeart_rate);
         ArrayAdapter<Heart_Rate> arrayAdapter
                = new ArrayAdapter<Heart_Rate>(this, android.R.layout.simple_list_item_1 , (Heart_Rate[]) mHeart_rate.toArray(new Heart_Rate[mHeart_rate.size()]));
         beatHis.setAdapter(arrayAdapter);

    }


}
