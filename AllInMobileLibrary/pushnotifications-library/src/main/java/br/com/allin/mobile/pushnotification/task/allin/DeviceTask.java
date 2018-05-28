package br.com.allin.mobile.pushnotification.task.allin;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.http.RequestType;
import br.com.allin.mobile.pushnotification.http.Routes;
import br.com.allin.mobile.pushnotification.identifiers.HttpBodyIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.SystemIdentifier;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Thread for device information request
 */
public class DeviceTask extends BaseTask<String> {
    private String oldToken;
    private String newToken;

    public DeviceTask(String oldToken, String newToken, OnRequest onRequest) {
        super(RequestType.POST, true, onRequest);

        this.oldToken = oldToken;
        this.newToken = newToken;
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + Routes.DEVICE;
    }

    @Override
    public String[] getParams() {
        if (!Util.isNullOrClear(this.oldToken)) {
            return new String[] { Routes.UPDATE, this.oldToken };
        }

        return null;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyIdentifier.DEVICE_TOKEN, this.newToken);
            data.put(HttpBodyIdentifier.PLATFORM, SystemIdentifier.ANDROID);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(AIResponse response) {
        return response.getMessage();
    }
}
