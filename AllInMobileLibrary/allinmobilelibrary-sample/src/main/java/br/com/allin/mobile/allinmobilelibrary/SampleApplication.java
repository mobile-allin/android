package br.com.allin.mobile.allinmobilelibrary;

import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import br.com.allin.mobile.pushnotification.AllInApplication;
import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.model.ConfigurationOptions;
import br.com.allin.mobile.pushnotification.model.NotificationSettings;
import io.fabric.sdk.android.Fabric;

public class SampleApplication extends AllInApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            ConfigurationOptions configurationOptions = new ConfigurationOptions(getString(R.string.project_id));
            configurationOptions.setNotificationSettings(
                    new NotificationSettings("#000000",
                            R.mipmap.ic_launcher, android.R.drawable.sym_def_app_icon));

            AllInPush.configure(this, configurationOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void onActionNotification(String action) {
        Toast.makeText(SampleApplication.this, "onActionNotification", Toast.LENGTH_LONG).show();
    }
}
