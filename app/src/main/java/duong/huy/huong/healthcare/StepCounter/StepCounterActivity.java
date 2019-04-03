package duong.huy.huong.healthcare.StepCounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import duong.huy.huong.healthcare.R;

public class StepCounterActivity extends AppCompatActivity {
    ImageView headerCircle;
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

    }
        private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // call updateUI passing in our intent which is holding the data to display.
            TextView t = (TextView) findViewById(R.id.num_step);
            t.setText(String.valueOf(intent.getStringExtra("numSteps")) + " Bước");
        }
    };
}
