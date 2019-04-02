package duong.huy.huong.healthcare.StepCounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import duong.huy.huong.healthcare.R;

public class StepCounterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        getSupportActionBar().hide();
    }
}
