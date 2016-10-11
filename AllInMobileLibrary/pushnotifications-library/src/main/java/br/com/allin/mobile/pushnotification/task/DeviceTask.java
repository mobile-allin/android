package br.com.allin.mobile.pushnotification.task;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Parameters;
import br.com.allin.mobile.pushnotification.constants.Preferences;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for device information request
 */
public class DeviceTask extends BaseTask<String> {
    private DeviceEntity deviceEntity;

    public DeviceTask(DeviceEntity deviceEntity,
                      Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        this.deviceEntity = deviceEntity;
    }

    @Override
    public String getUrl() {
        return Route.DEVICE;
    }

    @Override
    public String[] getParams() {
        boolean renew = this.deviceEntity != null &&
                !TextUtils.isEmpty(this.deviceEntity.getDeviceId()) && this.deviceEntity.isRenewId();

        return renew ? new String[] { Route.UPDATE, deviceEntity.getDeviceId() } : null;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBody.DEVICE_TOKEN, AllInPush.getInstance().getDeviceId());
            data.put(HttpBody.PLATFORM, Parameters.ANDROID);

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
