package duong.huy.huong.healthcare;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import duong.huy.huong.healthcare.HeartRateMonitor.HeartRateActivity;
import duong.huy.huong.healthcare.RouteTracker.RouteTrackerActivity;
import duong.huy.huong.healthcare.SleepRecorder.SleepRecorderActivity;
import duong.huy.huong.healthcare.StepCounter.StepCounterActivity;
import duong.huy.huong.healthcare.StepCounter.StepCounterSrv;
import duong.huy.huong.healthcare.db.User_Info;
import duong.huy.huong.healthcare.db.User_InfoDao;
import duong.huy.huong.healthcare.remindService.MotionRemindService;

/**
 * Lớp này là activity mặc định khi khởi chạy. Nó chứa các fragment: Home(mặc định), Remind.
 * Và thanh navigation Bar để lựa chọn các fragment.
 *
 */
public class MainActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener, Remind.OnFragmentInteractionListener,UserInfoFragment.OnFragmentInteractionListener {

    Intent mStepCounterIntert;
    Intent mRouteTrackerIntent;
    Intent mHeartRateIntent;
    Intent mSleepRecorderIntent;
    TextView step;
    TextView calo;
    Intent srv; // service cho motionReminder
    Switch onOffSwitch1; // switch của nhắc vận động
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
                    mFragment = new Home();
                    loadFragment(mFragment);
                    return true;
                case R.id.navigation_remind:
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

    /**
     * Hàm onclick của nút mở giao diện đếm bước chân.
     * @param v
     */
    public void stepCounterOnclick(View v) {
        mStepCounterIntert = new Intent(this, StepCounterActivity.class);
        startActivity(mStepCounterIntert);

    }

    /**
     * Hàm onclick của nút lưu thông tin người dùng.
     * @param v
     */
    public void saveUserInfoOnclick(View v) {
        EditText nameEdt = (EditText)findViewById(R.id.edName);
        EditText heightEdt =(EditText) findViewById(R.id.edHeight);
        EditText weightEdt =(EditText) findViewById(R.id.edWeight);
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(nameEdt.getText());
        User_Info mUser_info;
        ArrayList<User_Info> mUser_infos = User_InfoDao.loadAllRecords();
        if(mUser_infos.size() >0) {
            mUser_info = mUser_infos.get(0);
            mUser_info.setheight(String.valueOf(heightEdt.getText()));
            mUser_info.setweight(String.valueOf(weightEdt.getText()));
            mUser_info.setname(String.valueOf(nameEdt.getText()));
            User_InfoDao.updateRecord(mUser_info);
        } else{
            mUser_info = new User_Info();
            mUser_info.setheight(String.valueOf(heightEdt.getText()));
            mUser_info.setweight(String.valueOf(weightEdt.getText()));
            mUser_info.setname(String.valueOf(nameEdt.getText()));
            User_InfoDao.insertRecord(mUser_info);
        }
        Snackbar
                .make(findViewById(R.id.main_layout), "Cập nhật thành công!", Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * Hàm onclick của nút mở giao diện đo nhịp tim.
     * @param v
     */
    public void heartRateOnclick(View v) {
        mHeartRateIntent = new Intent(this, HeartRateActivity.class);
        startActivity(mHeartRateIntent);

    }

    /**
     * hàm mở giao diện theo dõi giấc ngủ.
     * @param v
     */
    public void sleepRecorderOnclick(View v) {
        mSleepRecorderIntent = new Intent(this, SleepRecorderActivity.class);
        startActivity(mSleepRecorderIntent);

    }

    /**
     * Hàm mở giao diện theo dõi hành trình.
     * @param v
     */
    public void routeTrackerOnclick(View v) {
        mRouteTrackerIntent = new Intent(this, RouteTrackerActivity.class);
        startActivity(mRouteTrackerIntent);
    }
    /**
     * Hàm khởi tạo đối tượng.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new Home()); // load fragment mặc định
        step = (TextView) findViewById(R.id.textView7);
        calo = (TextView) findViewById(R.id.textView5);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.getSupportActionBar().hide();// ẩn actionbar
        registerReceiver(broadcastReceiver, new IntentFilter("duong.huy.huong.stepcounterbroadcast"));
    }

    /**
     * gán sự kiện cho các nút phần nhắc nhở.
     */
    public void motionRemindOnclick(View v) {
        if(!isMyServiceRunning(MotionRemindService.class)) {
            srv = new Intent(getBaseContext(), MotionRemindService.class);
            startService(srv);
        }else {
            if(isMyServiceRunning(MotionRemindService.class)) {
                stopService(srv);
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    /**
    * Hàm dùng để nhận callback từ các fragment con.
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Nhận broadcast bước chân
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(step != null && calo != null) {
                step.setText(String.valueOf(intent.getStringExtra("numSteps")));
                calo.setText(String.valueOf((float)Math.round(0.45*Integer.parseInt(intent.getStringExtra("numSteps")))/10) + " calo");
            } else {
                step = (TextView) findViewById(R.id.textView7);
                calo = (TextView) findViewById(R.id.textView5);
            }

        }

    };
}
