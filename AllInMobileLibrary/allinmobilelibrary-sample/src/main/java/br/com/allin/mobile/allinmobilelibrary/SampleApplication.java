package br.com.allin.mobile.allinmobilelibrary;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import io.fabric.sdk.android.Fabric;

public class SampleApplication extends Application implements AllInDelegate {
    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        AlliNPush.getInstance().registerForPushNotifications(this.getApplicationContext(), this);
    }

    @Override
    public void onSilentMessageReceived(String s) {
    }

    @Override
    public void onClickAction(String s) {
    }
}