package duong.huy.huong.healthcare;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import duong.huy.huong.healthcare.HeartRateMonitor.HeartRateActivity;
import duong.huy.huong.healthcare.RouteTracker.RouteTrackerActivity;
import duong.huy.huong.healthcare.SleepTracker.SleepTrackerActivity;
import duong.huy.huong.healthcare.StepCounter.StepCounterActivity;

/**
 * Lớp này là activity mặc định khi khởi chạy. Nó chứa các fragment: Home(mặc định), Remind.
 * Và thanh navigation Bar để lựa chọn các fragment.
 *
 */
public class MainActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener, Remind.OnFragmentInteractionListener,UserInfoFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    //StepCounterSrv StepCountingService;
//    private Button testMapButton;
//    private Button testHeartRateButton;
//    Intent intent;
//    Intent mapIntent;
//    Intent heartRateIntent;
    Intent mStepCounterIntert;
    Intent mRouteTrackerIntent;
    Intent mHeartRateIntent;
    Intent mSleepTrackerIntent;
    /**
     * Bắt sự kiện click thanh điều hướng chân màn hình.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragment;
            switch (item.getItemId()) {
                case R.id.navigation_tracking:
                    //mTextMessage.setText("abc");
                    mFragment = new Home();
                    loadFragment(mFragment);
                    return true;
                case R.id.navigation_remind:
                    //mTextMessage.setText("abc");
                    mFragment = new Remind();
                    loadFragment(mFragment);

                    return true;
                case R.id.navigation_user_info:
                    mFragment = new UserInfoFragment();
                    loadFragment(mFragment);
                    return true;
            }
            return false;
        }
    };

    /**
     * Hàm này để load lại fragment. (ví dụ load lại fragment khi click vào thanh điều hướng chân trang.)
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
    public void stepCounterOnclick(View v) {
        mStepCounterIntert = new Intent(this, StepCounterActivity.class);
        startActivity(mStepCounterIntert);

    }
    public void heartRateOnclick(View v) {
        mHeartRateIntent = new Intent(this, HeartRateActivity.class);
        startActivity(mHeartRateIntent);

    }
    public void sleepTrackerOnclick(View v) {
        mSleepTrackerIntent = new Intent(this, SleepTrackerActivity.class);
        startActivity(mSleepTrackerIntent);

    }
    public void routeTrackerOnclick(View v) {
        //mStepCounterIntert = new Intent(this, StepCounterActivity.class);
        //startActivity(mStepCounterIntert);
        mRouteTrackerIntent = new Intent(this, RouteTrackerActivity.class);
        startActivity(mRouteTrackerIntent);
    }
    /**
     * Hàm gọi 1 lần khi khởi tạo đối tượng.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new Home());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.getSupportActionBar().hide();

        //registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.stepcounterbroadcast"));
//        registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.stepcounterbroadcast"));
//        intent = new Intent("duong.huy.huong.stepcounterbroadcast");
//
//        mapIntent = new Intent(this, RouteTrackerActivity.class);
//        testMapButton = (Button) findViewById(R.id.testMapButton);
//        testMapButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(mapIntent);
//            }
//        });
//        testHeartRateButton = (Button) findViewById(R.id.testHeartRateButton);
//        heartRateIntent = new Intent(this, HeartRateActivity.class);
//        testHeartRateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(heartRateIntent);
//            }
//        });
    }
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // call updateUI passing in our intent which is holding the data to display.
//            TextView t = (TextView) findViewById(R.id.test_counter);
//            t.setText(String.valueOf(intent.getStringExtra("numSteps")) + " bước");
//        }
//    };

    /**
    * Hàm dùng để nhận callback từ các fragment con.
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
//    public void testDB(){
//        Step st = StepDao.loadRecordByStep_Date(String.valueOf(Calendar.getInstance().getTime().getTime()/60/1000/24));
//        if(st == null) {
//            TextView t = (TextView) findViewById(R.id.textView7);
//            t.setText("null");
//        } else {
//            TextView t = (TextView) findViewById(R.id.textView7);
//            t.setText(st.getstep());
//        }
//
//
//
//    }

    /**
     * Nhận broadcast bước chân
     */
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // call updateUI passing in our intent which is holding the data to display.
//            TextView t = (TextView) findViewById(R.id.textView7);
//            t.setText(String.valueOf(intent.getStringExtra("numSteps")) + " Bước");
//            testDB();
//            //if(!isMyServiceRunning())
//        }
//
//    };
}
