package br.com.allin.mobile.pushnotification.configuration;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.identifiers.BroadcastNotificationIdentifier;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.notification.BroadcastNotification;

/**
 * Created by lucasrodrigues on 25/08/17.
 */

public class AlliNConfiguration {
    private AlliNConfiguration() {
    }

    private static AlliNConfiguration alliNConfiguration;

    public static AlliNConfiguration getInstance() {
        if (AlliNConfiguration.alliNConfiguration == null) {
            AlliNConfiguration.alliNConfiguration = new AlliNConfiguration();
        }

        return AlliNConfiguration.alliNConfiguration;
    }

    private AllInDelegate allInDelegate;
    private BroadcastNotification broadcastNotification;

    public AllInDelegate getDelegate() {
        return this.allInDelegate;
    }

    public void initialize(AllInDelegate allInDelegate) {
        this.allInDelegate = allInDelegate;
        this.broadcastNotification = new BroadcastNotification();

        Context context = AlliNPush.getInstance().getContext();
        IntentFilter intentFilter = new IntentFilter(BroadcastNotificationIdentifier.ACTION);

        context.registerReceiver(this.broadcastNotification, intentFilter);
    }

    public void finish() {
        try {
            Context context = AlliNPush.getInstance().getContext();
            context.unregisterReceiver(this.broadcastNotification);
        } catch (Exception e) {
            Log.i("BroadcastNotification", "Receiver not registered");
        }

        this.broadcastNotification = null;
    }
}
