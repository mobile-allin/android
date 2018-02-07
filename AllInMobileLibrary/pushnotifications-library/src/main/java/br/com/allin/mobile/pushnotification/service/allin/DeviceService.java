package br.com.allin.mobile.pushnotification.service.allin;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.ListConstant;
import br.com.allin.mobile.pushnotification.constants.ParametersConstant;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstant;
import br.com.allin.mobile.pushnotification.entity.allin.BaseEntity;
import br.com.allin.mobile.pushnotification.entity.allin.DeviceEntity;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.helper.Util;
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

    public DeviceService() {
    }

    public DeviceService(OnRequest onRequest) {
        this.onRequest = onRequest;
    }

    public void sendDevice(final DeviceEntity deviceEntity) {
        new DeviceTask(deviceEntity, new OnRequest() {
            @Override
            public void onFinish(Object value) {
                String pushId = AlliNPush.getInstance().getDeviceToken();
                Map<String, String> map = new HashMap<>();
                map.put(ListConstant.ID_PUSH, Util.md5(pushId));
                map.put(ListConstant.PUSH_ID, pushId);
                map.put(ListConstant.PLATAFORMA, ParametersConstant.ANDROID);

//                AlliNPush.getInstance().sendList(ListConstant.LISTA_PADRAO, map);
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
        new LogoutTask(onRequest).execute();
    }

    public void sendList(String nameList, List<BaseEntity> columnsAndValues) {
        new ListTask(nameList, columnsAndValues, onRequest).execute();
    }

    public void registerEmail(String email) {
        new EmailTask(email, onRequest).execute();
    }

    public String getDeviceToken() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        return preferencesManager.getData(PreferencesConstant.KEY_DEVICE_ID, null);
    }

    public String getEmail() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        return preferencesManager.getData(PreferencesConstant.KEY_USER_EMAIL, null);
    }

    public String getIdentifier() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);
        String identifier = preferencesManager.getData(PreferencesConstant.KEY_IDENTIFIER, null);

        if (Util.isNullOrClear(identifier)) {
            identifier = Util.md5(UUID.randomUUID().toString());
            preferencesManager.storeData(PreferencesConstant.KEY_IDENTIFIER, identifier);
        }

        return identifier;
    }

    public DeviceEntity getDeviceInfos(String senderId) {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        String deviceId = preferencesManager.getData(PreferencesConstant.KEY_DEVICE_ID, null);
        Integer appVersion = preferencesManager.getData(PreferencesConstant.KEY_APPVERSION, 1);
        String projectId = preferencesManager.getData(PreferencesConstant.KEY_PROJECT_ID, null);

        if (Util.isNullOrClear(deviceId)) {
            return null;
        }

        return new DeviceEntity(deviceId,
                appVersion != Util.getAppVersion(context) || !senderId.equals(projectId));
    }
}
