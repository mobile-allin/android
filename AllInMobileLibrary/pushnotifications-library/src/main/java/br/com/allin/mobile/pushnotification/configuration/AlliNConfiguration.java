package br.com.allin.mobile.pushnotification.configuration;

import android.content.IntentFilter;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.BroadcastNotificationIdentifier;
import br.com.allin.mobile.pushnotification.fcm.BroadcastNotification;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;

/**
 * Created by lucasrodrigues on 25/08/17.
 */

public class AlliNConfiguration {
    private AlliNConfiguration() {
    }

    private static AlliNConfiguration alliNConfiguration;

    public static AlliNConfiguration getInstance() {
        if (alliNConfiguration == null) {
            alliNConfiguration = new AlliNConfiguration();
        }

        return alliNConfiguration;
    }

    private AllInDelegate allInDelegate;
    private BroadcastNotification broadcastNotification;

    public AllInDelegate getDelegate() {
        return allInDelegate;
    }

    public void init(AllInDelegate allInDelegate) {
        this.allInDelegate = allInDelegate;
        this.broadcastNotification = new BroadcastNotification();

        AlliNPush.getInstance().getContext()
                .registerReceiver(broadcastNotification,
                        new IntentFilter(BroadcastNotificationIdentifier.ACTION));
    }

    public void finish() {
        AlliNPush.getInstance().getContext().unregisterReceiver(broadcastNotification);

        broadcastNotification = null;
    }
}
