package br.com.allin.mobile.pushnotification.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by lucasrodrigues on 18/01/18.
 */

public class UpdateListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
