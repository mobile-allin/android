package br.com.allin.mobile.pushnotification.service;

import android.content.Context;
import android.text.TextUtils;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.entity.NotificationEntity;

/**
 * Service class for push configuration
 */
public class ConfigurationService {
    private Context context;
    private ConfigurationEntity configurationEntity;
    private NotificationEntity notificationEntity;
    private SharedPreferencesManager sharedPreferencesManager;

    public ConfigurationService(ConfigurationEntity configurationEntity) {
        this.context = AlliNPush.getInstance().getContext();
        this.configurationEntity = configurationEntity;
        this.notificationEntity = configurationEntity.getNotification();
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    public void init() {
        new CacheService(context).sync();

        if (notificationEntity != null) {
            sharedPreferencesManager.storeData(PreferencesConstants.KEY_ICON_NOTIFICATION,
                    notificationEntity.getIcon());
            sharedPreferencesManager.storeData(PreferencesConstants.KEY_WHITE_ICON_NOTIFICATION,
                    notificationEntity.getWhiteIcon());
            sharedPreferencesManager.storeData(PreferencesConstants.KEY_BACKGROUND_NOTIFICATION,
                    notificationEntity.getBackground());
        }

        DeviceEntity deviceEntity =
                new DeviceService(context).getDeviceInfos(this.configurationEntity.getSenderId());

        if (deviceEntity == null || TextUtils
                .isEmpty(deviceEntity.getDeviceId()) || deviceEntity.isRenewId()) {
            new GCMService(deviceEntity, this.context, configurationEntity, null).execute();
        } else {
            new DeviceService(this.context, null).sendDevice(deviceEntity);
        }
    }
}
