package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.Parameters;
import br.com.allin.mobile.pushnotification.constants.Preferences;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.DeviceTask;
import br.com.allin.mobile.pushnotification.task.EmailTask;
import br.com.allin.mobile.pushnotification.task.ListTask;
import br.com.allin.mobile.pushnotification.task.LogoutTask;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class DeviceService {
    private Context context;
    private OnRequest onRequest;

    public DeviceService(Context context) {
        this.context = context;
        this.onRequest = null;
    }

    public DeviceService(Context context, OnRequest onRequest) {
        this.context = context;
        this.onRequest = onRequest;
    }

    public void sendDeviceInfo(final DeviceEntity deviceEntity) {
        new DeviceTask(deviceEntity, context, new OnRequest() {
            @Override
            public void onFinish(Object value) {
                String pushId = AllInPush.getInstance().getDeviceId();
                Map<String, String> map = new HashMap<>();
                map.put("id_push", Util.md5(pushId));
                map.put("push_id", pushId);
                map.put("plataforma", Parameters.ANDROID);

                sendList("Lista Padrao Push", map);
            }

            @Override
            public void onError(Exception exception) {
                if (onRequest != null) {
                    onRequest.onError(exception);
                }
            }
        }).execute();
    }

    public void logout() {
        new LogoutTask(context, onRequest).execute();
    }

    public void sendList(String nameList, Map<String, String> values) {
        new ListTask(nameList, values, context, onRequest).execute();
    }

    public void updateEmail(String email) {
        new EmailTask(email, context, onRequest).execute();
    }

    public String getDeviceToken() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String deviceToken = sharedPreferencesManager.getData(Preferences.DEVICE_ID, null);

        return deviceToken != null && deviceToken.trim().length() > 0 ? deviceToken : null;
    }

    public String getUserEmail() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String userEmail = sharedPreferencesManager.getData(Preferences.USER_EMAIL, null);

        return userEmail != null && userEmail.trim().length() > 0 ? userEmail : null;
    }

    public DeviceEntity getDeviceInfos(String senderId) {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);

        String deviceId = sharedPreferencesManager.getData(Preferences.DEVICE_ID, null);
        Integer registeredVersion = sharedPreferencesManager.getData(Preferences.APPVERSION, 1);
        String sharedProjectId = sharedPreferencesManager.getData(Preferences.PROJECT_ID, null);

        if (Util.isNullOrClear(deviceId)) {
            return null;
        }

        return new DeviceEntity(deviceId,
                registeredVersion != Util.getAppVersion(context) || !senderId.equals(sharedProjectId));
    }
}
