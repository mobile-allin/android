package br.com.allin.mobile.allinmobilelibrary;

import android.app.Application;

import br.com.allin.mobile.pushnotification.AlliNPush;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AlliNPush.getInstance().registerForPushNotifications(this.getApplicationContext());
    }
}