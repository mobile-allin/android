package br.com.allin.mobile.pushnotification.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.constants.NotificationConstants;
import br.com.allin.mobile.pushnotification.enumarator.Action;
import br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity;

/**
 * Created by lucasrodrigues on 9/20/16.
 */
public class BroadcastNotification extends BroadcastReceiver {
    public static final String BROADCAST_NOTIFICATION = "BroadcastNotification";

    @Override
    public void onReceive(Context context, Intent intentReceiver) {
        AllInPush.registerNotificationAction(Action.CLICK, null);

        Bundle extras = intentReceiver.getExtras();

        Intent intent = new Intent(context, AllInWebViewActivity.class);
        intent.putExtras(extras);
        intent.putExtra(NotificationConstants.SUBJECT,
                extras.getString(NotificationConstants.SUBJECT));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
