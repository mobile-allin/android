package br.com.allin.mobile.pushnotification.configurations;

import android.content.Context;
import android.content.IntentFilter;

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
    private BroadcastNotification broadcastNotification;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AllInDelegate getAllInDelegate() {
        return allInDelegate;
    }

    public void setAllInDelegate(AllInDelegate allInDelegate) {
        this.allInDelegate = allInDelegate;
    }

    public void init() {
        broadcastNotification = new BroadcastNotification();

        context.registerReceiver(broadcastNotification, new IntentFilter(BroadcastNotification.ACTION));
    }

    public void finish() {
        context.unregisterReceiver(broadcastNotification);

        broadcastNotification = null;
    }
}
