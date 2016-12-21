package br.com.allin.mobile.pushnotification.service;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.exception.GenerateDeviceIdException;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * The service class for GCM (Google Cloud Message) has the responsibility 
 * of the sender's ID number generator for Google properly send notification according to the record
 */
public class GCMService extends AsyncTask<Void, Void, String> {
    private Context context;
    private ConfigurationEntity configurationEntity;
    private OnRequest<String> onRequest;
    private DeviceEntity deviceEntity;

    public GCMService(DeviceEntity deviceEntity, Context context,
                      ConfigurationEntity configurationEntity,
                      OnRequest<String> onRequest) {
        this.context = context;
        this.configurationEntity = configurationEntity;
        this.onRequest = onRequest;
        this.deviceEntity = deviceEntity;
    }

    @Override
    protected String doInBackground(Void... params) {
        InstanceID instanceID = InstanceID.getInstance(this.context);

        String token = null;

        for (int attempts = 0; attempts < 3 &&
                (token == null || TextUtils.isEmpty(token)); attempts++) {
            try {
                token = instanceID.getToken(this.configurationEntity.getSenderId(),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                if (this.onRequest != null) {
                    this.onRequest.onError(
                            new GenerateDeviceIdException(
                                    this.configurationEntity.getSenderId(), e.getMessage()));
                } else {
                    e.printStackTrace();
                }

                return null;
            }
        }

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this.context);
        sharedPreferencesManager.storeData(PreferencesConstants.KEY_DEVICE_ID, token);
        sharedPreferencesManager.storeData(PreferencesConstants.KEY_PROJECT_ID, this.configurationEntity.getSenderId());

        return token;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null && !TextUtils.isEmpty(s)) {
            AllInPush.getInstance().sendDeviceInfo(this.deviceEntity, this.onRequest);
        }
    }
}
