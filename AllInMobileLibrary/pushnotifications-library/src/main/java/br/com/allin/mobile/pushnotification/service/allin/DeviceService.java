package br.com.allin.mobile.pushnotification.service.allin;

import android.content.Context;

import java.util.List;
import java.util.UUID;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.dao.AlliNDatabase;
import br.com.allin.mobile.pushnotification.entity.allin.AIList;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
import br.com.allin.mobile.pushnotification.helper.ListPersistence;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.identifiers.ListIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.SystemIdentifier;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.allin.DeviceTask;
import br.com.allin.mobile.pushnotification.task.allin.ListTask;
import br.com.allin.mobile.pushnotification.task.allin.LogoutTask;

/**
 * Service class for device information and configuration
 */
@SuppressWarnings("all")
public class DeviceService {
    private OnRequest onRequest;

    public void setOnRequest(OnRequest onRequest) {
        this.onRequest = onRequest;
    }

    public void sendDevice(String oldToken, String newToken) {
        setDeviceToken(newToken);

        if (Util.isEmpty(oldToken) || !oldToken.equals(newToken)) {
            new DeviceTask(oldToken, newToken, this.onRequest).execute();
        }
    }

    public void sendList(String nameList, List<AIValues> columnsAndValues) {
        this.updateList(columnsAndValues);

        String md5 = ListPersistence.getMD5(nameList, columnsAndValues);

        if (AlliNDatabase.get().listTable().exist(md5) == 0) {
            AlliNDatabase.get().listTable().insert(new AIList(md5));

            new ListTask(nameList, columnsAndValues, this.onRequest).execute();
        }
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

    public String getIdentifier() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);
        String identifier = preferencesManager.getData(PreferenceIdentifier.IDENTIFIER, null);

        if (Util.isEmpty(identifier)) {
            identifier = Util.md5(UUID.randomUUID().toString());
            preferencesManager.storeData(PreferenceIdentifier.IDENTIFIER, identifier);
        }

        return identifier;
    }

    private void updateList(List<AIValues> columnsAndValues) {
        boolean containPushId = false;
        boolean containPlatform = false;

        for (AIValues values : columnsAndValues) {
            if (values.getKey().equalsIgnoreCase(ListIdentifier.PUSH_ID)) {
                containPushId = true;
            } else if (values.getKey().equalsIgnoreCase(ListIdentifier.PLATAFORMA)) {
                containPlatform = true;
            }
        }

        String pushId = AlliNPush.getInstance().getDeviceToken();

        if (!containPushId) {
            columnsAndValues.add(new AIValues(ListIdentifier.PUSH_ID, pushId));
        }

        if (!containPlatform) {
            columnsAndValues.add(new AIValues(ListIdentifier.PLATAFORMA, SystemIdentifier.ANDROID));
        }
    }

    public void logout(String email) {
        new LogoutTask(email).execute();
    }
}
