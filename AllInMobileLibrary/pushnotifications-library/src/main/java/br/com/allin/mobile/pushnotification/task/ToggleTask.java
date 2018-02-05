package br.com.allin.mobile.pushnotification.task;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstant;
import br.com.allin.mobile.pushnotification.constants.RouteConstant;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for enable or disable push notification for device request
 */
public class ToggleTask extends BaseTask<String> {
    private boolean enable;

    public ToggleTask(boolean enable, OnRequest onRequest) {
        super(RequestType.POST, true, onRequest);

        this.enable = enable;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();
            data.put(HttpBodyConstant.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String getUrl() {
        return enable ? RouteConstant.DEVICE_ENABLE : RouteConstant.DEVICE_DISABLE;
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
