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
import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.constants.ActionConstant;
import br.com.allin.mobile.pushnotification.constants.NotificationConstant;
import br.com.allin.mobile.pushnotification.service.allin.NotificationService;
import br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity;

/**
 * Broadcast invoked after the user clicks the notification
 */
public class BroadcastNotification extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intentReceive) {
        Bundle extras = intentReceive.getExtras();

        if (extras == null) {
            return;
        }

        long idMessage = extras.getLong(NotificationConstant.ID, 0);

        if (idMessage > 0) {
            AlliNPush.getInstance().messageHasBeenRead((int) idMessage);
        }

        if (intentReceive.getStringExtra(ActionConstant.class.toString()) != null &&
                !TextUtils.isEmpty(intentReceive.getStringExtra(ActionConstant.class.toString()))) {
            if (AlliNConfiguration.getInstance().getAllInDelegate() != null) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        String action = intentReceive.getStringExtra(ActionConstant.class.toString());

                        AlliNConfiguration.getInstance().getAllInDelegate().onAction(action, false);
                    }
                }.sendEmptyMessage(0);
            }
        } else {
            if (intentReceive.hasExtra(NotificationConstant.ID_CAMPAIGN)) {
                int idCampaign = Integer.parseInt(extras.getString(NotificationConstant.ID_CAMPAIGN));
                String date = extras.getString(NotificationConstant.DATE_NOTIFICATION);

                new NotificationService().sendCampaign(idCampaign, date);
            } else if (intentReceive.hasExtra(NotificationConstant.ID_SEND)) {
                int idSend = Integer.parseInt(extras.getString(NotificationConstant.ID_SEND));
                String date = extras.getString(NotificationConstant.DATE_NOTIFICATION);

                new NotificationService().sendTransactional(idSend, date);
            }

            start(context, extras, intentReceive.hasExtra(NotificationConstant.URL_SCHEME));
        }
    }

    private void start(Context context, Bundle extras, boolean isScheme) {
        if (isScheme) {
            String urlScheme = extras.getString(NotificationConstant.URL_SCHEME);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlScheme));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            String subject = extras.getString(NotificationConstant.SUBJECT);

            Intent intent = new Intent(context, AllInWebViewActivity.class);
            intent.putExtras(extras);
            intent.putExtra(NotificationConstant.SUBJECT, subject);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}