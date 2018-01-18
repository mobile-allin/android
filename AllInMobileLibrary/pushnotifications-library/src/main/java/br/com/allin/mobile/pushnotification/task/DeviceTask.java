package br.com.allin.mobile.pushnotification.task;

import android.text.TextUtils;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.ParametersConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for device information request
 */
public class DeviceTask extends BaseTask<String> {
    private DeviceEntity deviceEntity;

    public DeviceTask(DeviceEntity deviceEntity, OnRequest onRequest) {
        super(RequestType.POST, true, onRequest);

        this.deviceEntity = deviceEntity;
    }

    @Override
    public String getUrl() {
        return RouteConstants.DEVICE;
    }

    @Override
    public String[] getParams() {
        boolean renew = this.deviceEntity != null &&
                !TextUtils.isEmpty(this.deviceEntity.getDeviceId()) && this.deviceEntity.isRenewId();

        return renew ? new String[] { RouteConstants.UPDATE, deviceEntity.getDeviceId() } : null;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyConstants.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());
            data.put(HttpBodyConstants.PLATFORM, ParametersConstants.ANDROID);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
