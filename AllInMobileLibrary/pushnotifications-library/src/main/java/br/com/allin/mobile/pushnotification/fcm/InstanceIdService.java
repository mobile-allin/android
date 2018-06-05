package br.com.allin.mobile.pushnotification.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.service.allin.DeviceService;

/**
 * This class is invoked when the Device Token is refreshed
 */
public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String oldToken = AlliNPush.getInstance(this).getDeviceToken();
        String newToken = FirebaseInstanceId.getInstance().getToken();

        new DeviceService().sendDevice(oldToken, newToken);
    }
}
