package br.com.allin.mobile.pushnotification.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.constants.Notification;
import br.com.allin.mobile.pushnotification.service.NotificationService;
import br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity;

/**
 * Created by lucasrodrigues on 9/20/16.
 */
public class BroadcastNotification extends BroadcastReceiver {
    public static final String BROADCAST_NOTIFICATION = "BroadcastNotification";

    @Override
    public void onReceive(Context context, Intent intentReceiver) {
        Bundle extras = intentReceiver.getExtras();

        if (intentReceiver.hasExtra(Notification.ID_CAMPAIGN)) {
            AllInPush.getInstance()
                    .notificationCampaign(extras.getInt(Notification.ID_CAMPAIGN));
        } else if (intentReceiver.hasExtra(Notification.ID_SEND)) {
            AllInPush.getInstance()
                    .notificationTransactional(extras.getInt(Notification.ID_CAMPAIGN));
        }

        Intent intent = new Intent(context, AllInWebViewActivity.class);
        intent.putExtras(extras);
        intent.putExtra(Notification.SUBJECT, extras.getString(Notification.SUBJECT));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
