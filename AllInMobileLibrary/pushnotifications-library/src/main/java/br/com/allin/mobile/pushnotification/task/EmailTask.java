package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.ParametersConstants;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

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
        return RouteConstants.EMAIL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();
            data.put(HttpBodyConstants.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());
            data.put(HttpBodyConstants.PLATFORM, ParametersConstants.ANDROID);
            data.put(HttpBodyConstants.USER_EMAIL, email);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        Context context = AlliNPush.getInstance().getContext();
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        sharedPreferencesManager.storeData(PreferencesConstants.KEY_USER_EMAIL, email);

        return responseEntity.getMessage();
    }
}
