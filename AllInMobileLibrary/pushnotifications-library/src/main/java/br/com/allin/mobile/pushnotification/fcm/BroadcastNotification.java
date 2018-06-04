package br.com.allin.mobile.pushnotification.fcm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.service.allin.NotificationService;
import br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity;

/**
 * Broadcast invoked after the user clicks the notification
 */
public class BroadcastNotification extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intentReceive) {
        Bundle bundle = intentReceive.getExtras();

        if (bundle != null) {
            if (bundle.containsKey(PushIdentifier.ID)) {
                AlliNPush.getInstance().messageHasBeenRead((int) bundle.getLong(PushIdentifier.ID));
            }

            if (bundle.containsKey(PushIdentifier.ACTION)) {
                AllInDelegate delegate = AlliNConfiguration.getInstance().getDelegate();

                if (delegate != null) {
                    delegate.onClickAction(bundle.getString(PushIdentifier.ACTION));
                }
            } else {
                sendNotificationType(bundle);
            }

            start(context, bundle);
        }
    }

    private void sendNotificationType(Bundle bundle) {
        NotificationService service = new NotificationService();
        String date = bundle.getString(PushIdentifier.DATE);

        if (bundle.containsKey(PushIdentifier.ID_CAMPAIGN)) {
            service.sendCampaign(bundle.getInt(PushIdentifier.ID_CAMPAIGN), date);
        } else if (bundle.containsKey(PushIdentifier.ID_SEND)) {
            service.sendTransactional(bundle.getInt(PushIdentifier.ID_SEND), date);
        }
    }

    private void start(Context context, Bundle bundle) {
        if (bundle.containsKey(PushIdentifier.URL_SCHEME)) {
            Uri uri = Uri.parse(bundle.getString(PushIdentifier.URL_SCHEME));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, AllInWebViewActivity.class);
            intent.putExtra(PushIdentifier.DATE, bundle.getString(PushIdentifier.DATE));
            intent.putExtra(PushIdentifier.ID_CAMPAIGN, bundle.getString(PushIdentifier.ID_CAMPAIGN));
            intent.putExtra(PushIdentifier.ID_LOGIN, bundle.getString(PushIdentifier.ID_LOGIN));
            intent.putExtra(PushIdentifier.SUBJECT, bundle.getString(PushIdentifier.SUBJECT));
            intent.putExtra(PushIdentifier.URL_SCHEME, bundle.getString(PushIdentifier.URL_SCHEME));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}