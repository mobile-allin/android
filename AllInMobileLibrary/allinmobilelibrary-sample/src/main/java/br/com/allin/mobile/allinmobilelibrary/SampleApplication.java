package br.com.allin.mobile.allinmobilelibrary;

import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import br.com.allin.mobile.pushnotification.AllInApplication;
import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.NotificationEntity;
import io.fabric.sdk.android.Fabric;

public class SampleApplication extends AllInApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            ConfigurationEntity configurationEntity =
                    new ConfigurationEntity(getString(R.string.project_id));
            configurationEntity.setNotificationEntity(
                    new NotificationEntity("#000000",
                            R.mipmap.ic_launcher, android.R.drawable.sym_def_app_icon));

            AllInPush.configure(this, configurationEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void onAction(String action, boolean sentFromServer) {
        Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
    }
}