package duong.huy.huong.healthcare.StepCounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

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
    }
}
