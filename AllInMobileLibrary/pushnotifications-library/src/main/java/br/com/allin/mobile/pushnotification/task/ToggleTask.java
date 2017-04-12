package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for enable or disable push notification for device request
 */
public class ToggleTask extends BaseTask<String> {
    private boolean enable;

    public ToggleTask(boolean enable,
                      Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        this.enable = enable;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();
            data.put(HttpBodyConstants.DEVICE_TOKEN, AllInPush.getInstance().getDeviceId());

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String getUrl() {
        return enable ? RouteConstants.DEVICE_ENABLE : RouteConstants.DEVICE_DISABLE;
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
