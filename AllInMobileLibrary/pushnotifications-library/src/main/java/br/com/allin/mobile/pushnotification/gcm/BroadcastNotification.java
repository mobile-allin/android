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

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.configurations.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.constants.ActionConstants;
import br.com.allin.mobile.pushnotification.constants.NotificationConstants;
import br.com.allin.mobile.pushnotification.service.NotificationService;
import br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity;

/**
 * Broadcast invoked after the user clicks the notification
 */
public class BroadcastNotification extends WakefulBroadcastReceiver {
    public static String ACTION = "br.com.allin.mobile.pushnotification.gcm.BroadcastNotification";

    @Override
    public void onReceive(final Context context, final Intent intentReceive) {
        Bundle extras = intentReceive.getExtras();

        if (extras == null) {
            return;
        }

        long idMessage = extras.getLong(NotificationConstants.ID, 0);

        if (idMessage > 0) {
            AlliNPush.getInstance().messageHasBeenRead(context, (int) idMessage);
        }

        if (intentReceive.getStringExtra(ActionConstants.class.toString()) != null &&
                !TextUtils.isEmpty(intentReceive.getStringExtra(ActionConstants.class.toString()))) {
            if (AlliNConfiguration.getInstance().getAllInDelegate() != null) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        String action = intentReceive.getStringExtra(ActionConstants.class.toString());

                        AlliNConfiguration.getInstance().getAllInDelegate().onAction(action, false);
                    }
                }.sendEmptyMessage(0);
            }
        } else {
            if (intentReceive.hasExtra(NotificationConstants.ID_CAMPAIGN)) {
                int idCampaign = Integer.parseInt(extras.getString(NotificationConstants.ID_CAMPAIGN));
                String date = extras.getString(NotificationConstants.DATE_NOTIFICATION);

                new NotificationService().sendCampaign(idCampaign, date);
            } else if (intentReceive.hasExtra(NotificationConstants.ID_SEND)) {
                int idSend = Integer.parseInt(extras.getString(NotificationConstants.ID_SEND));
                String date = extras.getString(NotificationConstants.DATE_NOTIFICATION);

                new NotificationService().sendTransactional(idSend, date);
            }

            start(context, extras, intentReceive.hasExtra(NotificationConstants.URL_SCHEME));
        }
    }

    private void start(Context context, Bundle extras, boolean isScheme) {
        if (isScheme) {
            String urlScheme = extras.getString(NotificationConstants.URL_SCHEME);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlScheme));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            String subject = extras.getString(NotificationConstants.SUBJECT);

            Intent intent = new Intent(context, AllInWebViewActivity.class);
            intent.putExtras(extras);
            intent.putExtra(NotificationConstants.SUBJECT, subject);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}