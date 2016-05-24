package br.com.allin.mobile.pushnotification.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.allin.mobile.pushnotification.Util;

/**
 * IntentService that it will be executed when a notification was received.
 */
public class AllInGcmListenerService extends GcmListenerService {
    private static final String EXTRA_TITLE_KEY = "AllInNotificationTitle";
    private static final String EXTRA_CONTENT_KEY = "AllInNotificationContent";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (!data.isEmpty()) {
            readContentFromNotification(data);
        }
    }

    private void readContentFromNotification(Bundle data) {
        String notificationTitle = data.getString(EXTRA_TITLE_KEY);
        String notificationContent = data.getString(EXTRA_CONTENT_KEY);

        if (!Util.isNullOrClear(notificationTitle) && !Util.isNullOrClear(notificationContent))
            AllInGcmNotification.showNotification(this, notificationTitle, notificationContent, data);
    }
}
