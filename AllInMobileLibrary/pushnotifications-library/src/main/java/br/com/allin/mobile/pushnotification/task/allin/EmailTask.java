package br.com.allin.mobile.pushnotification.task.allin;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.constants.ParametersConstant;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstant;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstant;
import br.com.allin.mobile.pushnotification.constants.RouteConstant;
import br.com.allin.mobile.pushnotification.entity.allin.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Thread for e-mail request
 */
public class EmailTask extends BaseTask<String> {
    private String email;

    public EmailTask(String email, OnRequest onRequest) {
        super(RequestType.POST, true, onRequest);

        this.email = email;
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + RouteConstant.EMAIL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();
            data.put(HttpBodyConstant.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());
            data.put(HttpBodyConstant.PLATFORM, ParametersConstant.ANDROID);
            data.put(HttpBodyConstant.USER_EMAIL, email);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);
        preferencesManager.storeData(PreferencesConstant.KEY_USER_EMAIL, email);

        return responseEntity.getMessage();
    }
}
