package br.com.allin.mobile.pushnotification.service.allin;

import android.text.TextUtils;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstant;
import br.com.allin.mobile.pushnotification.entity.allin.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.allin.DeviceEntity;
import br.com.allin.mobile.pushnotification.entity.allin.NotificationEntity;

/**
 * Service class for push configuration
 */
public class ConfigurationService {
    private ConfigurationEntity configurationEntity;
    private NotificationEntity notificationEntity;
    private PreferencesManager preferencesManager;

    public ConfigurationService(ConfigurationEntity configurationEntity) {
        this.configurationEntity = configurationEntity;
        this.notificationEntity = configurationEntity.getNotification();
        this.preferencesManager = new PreferencesManager(AlliNPush.getInstance().getContext());
    }

    public void init() {
        new CacheService().sync();

        if (notificationEntity != null) {
            preferencesManager.storeData(
                    PreferencesConstant.KEY_ICON_NOTIFICATION, notificationEntity.getIcon());
            preferencesManager.storeData(
                    PreferencesConstant.KEY_WHITE_ICON_NOTIFICATION, notificationEntity.getWhiteIcon());
            preferencesManager.storeData(
                    PreferencesConstant.KEY_BACKGROUND_NOTIFICATION, notificationEntity.getBackground());
        }

        DeviceService deviceService = new DeviceService();
        DeviceEntity deviceEntity = deviceService.getDeviceInfos(configurationEntity.getSenderId());

        if (deviceEntity == null || TextUtils.isEmpty(deviceEntity.getDeviceId()) || deviceEntity.isRenewId()) {
            new GCMService(deviceEntity, configurationEntity).execute();
        } else {
            new DeviceService().sendDevice(deviceEntity);
        }
    }
}
