package br.com.allin.mobile.pushnotification;

import android.app.Application;
import android.content.IntentFilter;

import br.com.allin.mobile.pushnotification.gcm.BroadcastNotification;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;

/**
 * @author lucasbrsilva
 *
 * Application class that owned startup broadcast notification
 * and interface action notification (push silent)
 *
 */
public abstract class AllInApplication extends Application implements AllInDelegate {
    private BroadcastNotification broadcastNotification;

    @Override
    public void onCreate() {
        super.onCreate();

        broadcastNotification = new BroadcastNotification();

        getApplicationContext().registerReceiver(broadcastNotification,
                new IntentFilter(BroadcastNotification.BROADCAST_NOTIFICATION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        getApplicationContext().unregisterReceiver(broadcastNotification);
    }
}
