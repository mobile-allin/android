package br.com.allin.mobile.pushnotification.gcm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.allin.mobile.pushnotification.AllInApplication;
import br.com.allin.mobile.pushnotification.Manager;
import br.com.allin.mobile.pushnotification.Util;

/**
 * IntentService that it will be executed when a notification was received.
 */
public class AllInGcmListenerService extends GcmListenerService {
    private Handler mHandler;

    private static final String EXTRA_TITLE_KEY = "AllInNotificationTitle";
    private static final String EXTRA_CONTENT_KEY = "AllInNotificationContent";
    private static final String EXTRA_NOTIFICATION_ACTION = "AllInNotificationAction";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (!data.isEmpty()) {
            readContentFromNotification(data);
        }
    }

    private void readContentFromNotification(Bundle data) {
        final String notificationTitle = data.getString(EXTRA_TITLE_KEY);
        final String notificationContent = data.getString(EXTRA_CONTENT_KEY);
        final String notificationAction = data.getString(EXTRA_NOTIFICATION_ACTION);

        if (!Util.isNullOrClear(notificationAction)) {
            if (Manager.getInstance().getApplication() instanceof AllInApplication) {
                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        AllInApplication allInApplication =
                                (AllInApplication) Manager.getInstance().getApplication();
                        allInApplication.onActionNotification(notificationAction);
                    }
                };

                mHandler.sendEmptyMessage(0);
            }
        } else if (!Util.isNullOrClear(notificationTitle) && !Util.isNullOrClear(notificationContent)) {
            AllInGcmNotification.showNotification(this, notificationTitle, notificationContent, data);
        }
    }
}
