package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
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

    public EmailTask(String email,
                     Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

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
            data.put(HttpBodyConstants.DEVICE_TOKEN, AllInPush.getInstance().getDeviceId());
            data.put(HttpBodyConstants.PLATFORM, ParametersConstants.ANDROID);
            data.put(HttpBodyConstants.USER_EMAIL, this.email);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this.context);
        sharedPreferencesManager.storeData(PreferencesConstants.KEY_USER_EMAIL, this.email);

        return responseEntity.getMessage();
    }
}
