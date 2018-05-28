package br.com.allin.mobile.allinmobilelibrary;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import io.fabric.sdk.android.Fabric;

public class SampleApplication extends Application implements AllInDelegate {
    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void onSilentMessageReceived(String identifier) {

    }

    @Override
    public void onClickAction(String identifier) {

    }
}