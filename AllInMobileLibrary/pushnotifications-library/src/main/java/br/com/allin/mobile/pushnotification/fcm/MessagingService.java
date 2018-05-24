package br.com.allin.mobile.pushnotification.fcm;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;
import java.util.Map;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.constants.PushIdentifier;
import br.com.allin.mobile.pushnotification.helper.Util;
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
                notification.showNotification(updateScheme(remoteMessage));
            }
        }
    }

    private Bundle updateScheme(RemoteMessage remoteMessage) {
        Map<String, String> map = remoteMessage.getData();

        if (map.containsKey(PushIdentifier.URL_SCHEME)) {
            String scheme = map.get(PushIdentifier.URL_SCHEME);

            try {
                scheme = URLDecoder.decode(scheme, "UTF-8");
            } catch (Exception e) {
                Log.e(AllInGcmNotification.class.toString(), "ERRO IN DECODE URL");
            } finally {
                if (scheme != null && scheme.contains("##id_push##")) {
                    String md5DeviceToken = Util.md5(AlliNPush.getInstance().getDeviceToken());

                    scheme = scheme.replace("##id_push##", md5DeviceToken);
                }

                map.put(PushIdentifier.URL_SCHEME, scheme);
            }
        }

        return convertToBundle(remoteMessage);
    }

    private Bundle convertToBundle(RemoteMessage remoteMessage) {
        Bundle bundle = new Bundle();

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        return bundle;
    }
}
