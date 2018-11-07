package br.com.allin.mobile.pushnotification.service.allin;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.identifiers.ListIdentifier;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.SystemIdentifier;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.allin.DeviceTask;
import br.com.allin.mobile.pushnotification.task.allin.EmailTask;
import br.com.allin.mobile.pushnotification.task.allin.ListTask;
import br.com.allin.mobile.pushnotification.task.allin.LogoutTask;

/**
 * Service class for device information and configuration
 */
public class DeviceService {
    private OnRequest onRequest;

    public void setOnRequest(OnRequest onRequest) {
        this.onRequest = onRequest;
    }

    public void sendDevice(String oldToken, String newToken) {
        setDeviceToken(newToken);

        new DeviceTask(oldToken, newToken, new OnRequest() {
            @Override
            public void onFinish(Object value) {
                String pushId = AlliNPush.getInstance().getDeviceToken();

                List<AIValues> list = new ArrayList<>();
                list.add(new AIValues(ListIdentifier.ID_PUSH, Util.md5(pushId)));
                list.add(new AIValues(ListIdentifier.PUSH_ID, pushId));
                list.add(new AIValues(ListIdentifier.PLATAFORMA, SystemIdentifier.ANDROID));

                AlliNPush.getInstance().sendList(ListIdentifier.LISTA_PADRAO, list);
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
        new LogoutTask(this.onRequest).execute();
    }

    public void sendList(String nameList, List<AIValues> columnsAndValues) {
        new ListTask(nameList, columnsAndValues, this.onRequest).execute();
    }

    public void registerEmail(String email) {
        new EmailTask(email, this.onRequest).execute();
    }

    public String getDeviceToken() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        return preferencesManager.getData(PreferenceIdentifier.DEVICE_TOKEN, null);
    }

    public void setDeviceToken(String deviceToken) {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        preferencesManager.storeData(PreferenceIdentifier.DEVICE_TOKEN, deviceToken);
    }

    public String getEmail() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        return preferencesManager.getData(PreferenceIdentifier.USER_EMAIL, null);
    }

    public String getIdentifier() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);
        String identifier = preferencesManager.getData(PreferenceIdentifier.IDENTIFIER, null);

        if (Util.isNullOrClear(identifier)) {
            identifier = Util.md5(UUID.randomUUID().toString());
            preferencesManager.storeData(PreferenceIdentifier.IDENTIFIER, identifier);
        }

        return identifier;
    }
}
