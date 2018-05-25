package br.com.allin.mobile.pushnotification.task.allin;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.identifiers.HttpBodyIdentifier;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.http.Routes;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.http.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.BaseTask;

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
            data.put(HttpBodyIdentifier.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN +
                (enable ? Routes.DEVICE_ENABLE : Routes.DEVICE_DISABLE);
    }

    @Override
    public String onSuccess(AIResponse AIResponse) {
        return AIResponse.getMessage();
    }
}
