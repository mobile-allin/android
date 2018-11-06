package br.com.allin.mobile.pushnotification.configurations;

import android.content.Context;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

import br.com.allin.mobile.pushnotification.gcm.BroadcastNotification;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;

/**
 * Created by lucasrodrigues on 25/08/17.
 */

public class AllInConfiguration {
    private AllInConfiguration() {
    }

    private static AllInConfiguration allInConfiguration;

    public static AllInConfiguration getInstance() {
        if (allInConfiguration == null) {
            allInConfiguration = new AllInConfiguration();
        }

        return allInConfiguration;
    }

    private AllInDelegate allInDelegate;
//    private BroadcastNotification broadcastNotification;
    private WeakReference<Context> context;

    public Context getContext() {
        if (this.context == null) {
            return null;
        }

        return this.context.get();
    }

    public void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    public AllInDelegate getAllInDelegate() {
        return allInDelegate;
    }

    public void setAllInDelegate(AllInDelegate allInDelegate) {
        this.allInDelegate = allInDelegate;
    }

    public void init() {
//        broadcastNotification = new BroadcastNotification();
//
//        context.registerReceiver(broadcastNotification, new IntentFilter(BroadcastNotification.ACTION));
    }

    public void finish() {
//        context.unregisterReceiver(broadcastNotification);
//
//        broadcastNotification = null;
    }
}
