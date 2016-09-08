package br.com.allin.mobile.pushnotification.gcm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.allin.mobile.pushnotification.AllInApplication;
import br.com.allin.mobile.pushnotification.Manager;
import br.com.allin.mobile.pushnotification.Util;
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
        final String subject = data.getString(NotificationConstants.SUBJECT);
        final String description = data.getString(NotificationConstants.DESCRIPTION);
        final String action = data.getString(NotificationConstants.ACTION);

        if (!Util.isNullOrClear(action)) {
            if (Manager.getInstance().getApplication() instanceof AllInApplication) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        AllInApplication allInApplication =
                                (AllInApplication) Manager.getInstance().getApplication();
                        allInApplication.onActionNotification(action);
                    }
                }.sendEmptyMessage(0);
            }
        } else if (!Util.isNullOrClear(subject) && !Util.isNullOrClear(description)) {
            AllInGcmNotification.showNotification(this, subject, description, data);
        }
    }
}
