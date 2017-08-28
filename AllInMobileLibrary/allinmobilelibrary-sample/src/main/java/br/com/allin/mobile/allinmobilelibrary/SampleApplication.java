package br.com.allin.mobile.allinmobilelibrary;

import android.app.Application;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.NotificationEntity;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import io.fabric.sdk.android.Fabric;

public class SampleApplication extends Application implements AllInDelegate {
    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void onAction(String action, boolean sentFromServer) {
        Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
    }
}