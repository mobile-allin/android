package br.com.allin.mobile.pushnotification.gcm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.constants.NotificationConstants;

/**
 * IntentService that it will be executed when a notification was received.
 */
public class AllInGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (!data.isEmpty()) {
            readContentFromNotification(data);
        }
    }

    private void readContentFromNotification(Bundle data) {
        final String action = data.getString(NotificationConstants.ACTION);

        if (!Util.isNullOrClear(data.getString(NotificationConstants.ACTION))) {
            if (AlliNConfiguration.getInstance().getAllInDelegate() != null) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        AlliNConfiguration.getInstance().getAllInDelegate().onAction(action, true);
                    }
                }.sendEmptyMessage(0);
            }
        } else {
            new AllInGcmNotification(this).showNotification(data);
        }
    }
}