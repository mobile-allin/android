package br.com.allin.mobile.pushnotification;

import android.app.Application;
import android.content.IntentFilter;

import br.com.allin.mobile.pushnotification.gcm.BroadcastNotification;
import br.com.allin.mobile.pushnotification.interfaces.OnActionNotification;

/**
 * Created by lucasrodrigues on 7/12/16.
 */
public abstract class AllInApplication extends Application implements OnActionNotification {
    private BroadcastNotification broadcastNotification;

    @Override
    public void onCreate() {
        super.onCreate();

        broadcastNotification = new BroadcastNotification();

        getApplicationContext().registerReceiver(broadcastNotification,
                new IntentFilter(BroadcastNotification.BROADCAST_NOTIFICATION));
    }

    @Override
    public void onAction(String action) {
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        getApplicationContext().unregisterReceiver(broadcastNotification);
    }
}
