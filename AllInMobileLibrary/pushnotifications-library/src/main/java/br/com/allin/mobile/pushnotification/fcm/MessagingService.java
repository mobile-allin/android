package br.com.allin.mobile.pushnotification.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage != null) {
            Map map = remoteMessage.getData();

            if (map.containsKey(PushIdentifier.SILENT_ACTION)) {
                AllInDelegate delegate = AlliNConfiguration.getInstance().getDelegate();

                if (delegate != null) {
                    delegate.onSilentMessageReceived(map.get(PushIdentifier.SILENT_ACTION).toString());
                }
            } else {
                Notification notification = new Notification(this);
                notification.showNotification(remoteMessage);
            }
        }
    }
}
