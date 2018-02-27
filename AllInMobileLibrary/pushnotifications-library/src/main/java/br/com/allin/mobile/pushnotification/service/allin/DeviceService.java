package br.com.allin.mobile.pushnotification.service.allin;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.ListConstant;
import br.com.allin.mobile.pushnotification.constants.ParametersConstant;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIDevice;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
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

    void sendDevice(final AIDevice AIDevice) {
        new DeviceTask(AIDevice, new OnRequest() {
            @Override
            public void onFinish(Object value) {
                String pushId = AlliNPush.getInstance().getDeviceToken();

                List<AIValues> list = new ArrayList<>();
                list.add(new AIValues(ListConstant.ID_PUSH, Util.md5(pushId)));
                list.add(new AIValues(ListConstant.PUSH_ID, pushId));
                list.add(new AIValues(ListConstant.PLATAFORMA, ParametersConstant.ANDROID));

                AlliNPush.getInstance().sendList(ListConstant.LISTA_PADRAO, list);
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

    public void sendList(String nameList, List<AIValues> columnsAndValues) {
        new ListTask(nameList, columnsAndValues, onRequest).execute();
    }

    public void registerEmail(String email) {
        new EmailTask(email, onRequest).execute();
    }

    public String getDeviceToken() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        return preferencesManager.getData(PreferencesConstant.DEVICE_ID, null);
    }

    public String getEmail() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        return preferencesManager.getData(PreferencesConstant.USER_EMAIL, null);
    }

    public String getIdentifier() {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);
        String identifier = preferencesManager.getData(PreferencesConstant.IDENTIFIER, null);

        if (Util.isNullOrClear(identifier)) {
            identifier = Util.md5(UUID.randomUUID().toString());
            preferencesManager.storeData(PreferencesConstant.IDENTIFIER, identifier);
        }

        return identifier;
    }

    AIDevice getDeviceInfos(String senderId) {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);

        String deviceId = preferencesManager.getData(PreferencesConstant.DEVICE_ID, null);
        Integer appVersion = preferencesManager.getData(PreferencesConstant.APP_VERSION, 1);
        String projectId = preferencesManager.getData(PreferencesConstant.PROJECT_ID, null);

        return new AIDevice(deviceId,
                appVersion != Util.getAppVersion(context) || !senderId.equals(projectId));
    }
}
