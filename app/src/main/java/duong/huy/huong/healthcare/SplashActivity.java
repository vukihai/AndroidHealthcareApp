package duong.huy.huong.healthcare;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import duong.huy.huong.healthcare.MainActivity;
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
        finish();

    }

}
