package br.com.allin.mobile.pushnotification.service.allin;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;

import java.lang.ref.WeakReference;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;

/**
 * The service class for FCM (Firebase Cloud MessageDatabaseConstant) has the responsibility
 * of the sender's ID number generator for Google properly send notification according to the record
 */
public class PushService extends AsyncTask<Void, Void, String> {
    private WeakReference<Context> context;

    public PushService() {
        this.context = new WeakReference<>(AlliNPush.getInstance().getContext());
    }

    @Override
    protected String doInBackground(Void... params) {
        return FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        new PreferencesManager(context.get()).storeData(PreferenceIdentifier.DEVICE_TOKEN, token);

        if (token != null && !TextUtils.isEmpty(token)) {
            new DeviceService().sendDevice(token);
        }
    }
}