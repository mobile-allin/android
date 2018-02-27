package br.com.allin.mobile.pushnotification.task.allin;

import android.text.TextUtils;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstant;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.constants.ParametersConstant;
import br.com.allin.mobile.pushnotification.constants.RouteConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIDevice;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Thread for device information request
 */
public class DeviceTask extends BaseTask<String> {
    private AIDevice AIDevice;

    public DeviceTask(AIDevice AIDevice, OnRequest onRequest) {
        super(RequestType.POST, true, onRequest);

        this.AIDevice = AIDevice;
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + RouteConstant.DEVICE;
    }

    @Override
    public String[] getParams() {
        boolean renew = this.AIDevice != null &&
                !TextUtils.isEmpty(this.AIDevice.getDeviceId()) && this.AIDevice.isRenewId();

        return renew ? new String[] { RouteConstant.UPDATE, AIDevice.getDeviceId() } : null;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyConstant.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());
            data.put(HttpBodyConstant.PLATFORM, ParametersConstant.ANDROID);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(AIResponse AIResponse) {
        return AIResponse.getMessage();
    }
}
