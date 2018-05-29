package br.com.allin.mobile.pushnotification.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.service.allin.DeviceService;

/**
 * This class is invoked when the Device Token is refreshed
 */
// dyGpAOIdVeA:APA91bH47sLZ2kdEy--mtiXbs6gHF2ydUbgZlB0HFopcwx-27CXuWSb1GHHMZ6DDeNX6MqUSeZ7pZCUpbVjIwyU-KW4_zoBpBi70a9i1pB1ZHhIQN3u4lb3NK7QebEPfEQG3jeIU_6Dz

public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String oldToken = AlliNPush.getInstance(this).getDeviceToken();
        String newToken = FirebaseInstanceId.getInstance().getToken();

        new DeviceService().sendDevice(oldToken, newToken);
    }
}
