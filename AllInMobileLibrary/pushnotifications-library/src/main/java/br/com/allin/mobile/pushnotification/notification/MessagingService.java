package br.com.allin.mobile.pushnotification.notification;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.service.allin.DeviceService;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Notification notification = new Notification(this);
        notification.showNotification(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);

        DeviceService deviceService = new DeviceService();
        deviceService.sendDevice(AlliNPush.getInstance(this).getDeviceToken(), newToken);
    }
}