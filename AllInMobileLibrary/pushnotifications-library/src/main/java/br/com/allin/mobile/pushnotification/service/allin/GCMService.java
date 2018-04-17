package br.com.allin.mobile.pushnotification.service.allin;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.entity.allin.AIConfiguration;
import br.com.allin.mobile.pushnotification.entity.allin.AIDevice;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstant;

/**
 * The service class for GCM (Google Cloud MessageConstant) has the responsibility
 * of the sender's ID number generator for Google properly send notification according to the record
 */
public class GCMService extends AsyncTask<Void, Void, String> {
    private AIConfiguration AIConfiguration;
    private AIDevice AIDevice;

    GCMService(AIDevice AIDevice, AIConfiguration AIConfiguration) {
        this.AIConfiguration = AIConfiguration;
        this.AIDevice = AIDevice;
    }

    @Override
    protected String doInBackground(Void... params) {
        InstanceID instanceID = InstanceID.getInstance(AlliNPush.getInstance().getContext());

        String token = null;

        for (int attempts = 0; attempts < 3 &&
                (token == null || TextUtils.isEmpty(token)); attempts++) {
            try {
                token = instanceID.getToken(AIConfiguration.getSenderId(),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        }

        Context context = AlliNPush.getInstance().getContext();
        String senderId = AIConfiguration.getSenderId();

        PreferencesManager preferencesManager = new PreferencesManager(context);
        preferencesManager.storeData(PreferencesConstant.DEVICE_TOKEN, token);
        preferencesManager.storeData(PreferencesConstant.PROJECT_ID, senderId);

        return token;
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        if (token != null && !TextUtils.isEmpty(token)) {
            AIDevice.setDeviceId(token);

            new DeviceService().sendDevice(AIDevice);
        }
    }
}