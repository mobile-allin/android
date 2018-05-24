package br.com.allin.mobile.pushnotification.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * This class is invoked when the Device Token is refreshed
 */
public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
