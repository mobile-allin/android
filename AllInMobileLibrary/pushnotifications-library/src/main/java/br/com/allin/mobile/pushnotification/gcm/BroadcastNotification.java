package br.com.allin.mobile.pushnotification.gcm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.configurations.AllInConfiguration;
import br.com.allin.mobile.pushnotification.constants.ActionConstants;
import br.com.allin.mobile.pushnotification.constants.NotificationConstants;
import br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity;

/**
 * Broadcast invoked after the user clicks the notification
 */
public class BroadcastNotification extends WakefulBroadcastReceiver {
    public static String ACTION = "br.com.allin.mobile.pushnotification.gcm.BroadcastNotification";

    @Override
    public void onReceive(final Context context, final Intent intentReceiver) {
        AllInPush.setContext(context);
        Bundle extras = intentReceiver.getExtras();

        long idMessage = extras.getLong(NotificationConstants.ID, 0);

        if (idMessage > 0) {
            AllInPush.getInstance().messageHasBeenRead((int) idMessage);
        }

        if (intentReceiver.getStringExtra(ActionConstants.class.toString()) != null && !TextUtils
                .isEmpty(intentReceiver.getStringExtra(ActionConstants.class.toString()))) {
            if (AllInConfiguration.getInstance().getAllInDelegate() != null) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        AllInConfiguration.getInstance().getAllInDelegate()
                                .onAction(intentReceiver.getStringExtra
                                        (ActionConstants.class.toString()), false);
                    }
                }.sendEmptyMessage(0);
            }
        } else {
            if (intentReceiver.hasExtra(NotificationConstants.ID_CAMPAIGN)) {
                int idCampaign = Integer
                        .parseInt(extras.getString(NotificationConstants.ID_CAMPAIGN));
                String date = extras.getString(NotificationConstants.DATE_NOTIFICATION);

                AllInPush.getInstance().notificationCampaign(idCampaign, date);
            } else if (intentReceiver.hasExtra(NotificationConstants.ID_SEND)) {
                int idSend = Integer.parseInt(extras.getString(NotificationConstants.ID_SEND));
                String date = extras.getString(NotificationConstants.DATE_NOTIFICATION);

                AllInPush.getInstance().notificationTransactional(idSend, date);
            }

            start(context, extras, intentReceiver.hasExtra(NotificationConstants.URL_SCHEME));
        }
    }

    private void start(Context context, Bundle extras, boolean isScheme) {
        if (isScheme) {
            String urlScheme = extras.getString(NotificationConstants.URL_SCHEME);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlScheme));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, AllInWebViewActivity.class);
            intent.putExtras(extras);
            intent.putExtra(NotificationConstants
                    .SUBJECT, extras.getString(NotificationConstants.SUBJECT));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}