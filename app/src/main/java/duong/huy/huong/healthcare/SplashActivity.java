package duong.huy.huong.healthcare;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import duong.huy.huong.healthcare.MainActivity;
import duong.huy.huong.healthcare.db.DbManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbManager.setConfig(this);
        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
        finish();

    }

}
