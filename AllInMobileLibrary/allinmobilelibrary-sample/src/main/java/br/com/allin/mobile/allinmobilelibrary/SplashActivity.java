package br.com.allin.mobile.allinmobilelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import br.com.allin.mobile.pushnotification.AlliNPush;

/**
 * Created by lucasrodrigues on 4/8/16.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }).start();

        if (AlliNPush.getInstance().getDeviceToken() != null) {
            Log.d("DEBUG 1", AlliNPush.getInstance().getDeviceToken());
        }
    }
}
