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
 * Thread for logout request
 */
public class LogoutTask extends BaseTask<String> {
    public LogoutTask(OnRequest onRequest) {
        super(RequestType.POST, true, onRequest);
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + Routes.DEVICE_LOGOUT;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyIdentifier.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());
            data.put(HttpBodyIdentifier.USER_EMAIL, AlliNPush.getInstance().getEmail());

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
