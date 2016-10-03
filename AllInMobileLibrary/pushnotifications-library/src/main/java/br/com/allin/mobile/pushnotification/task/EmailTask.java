package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Parameters;
import br.com.allin.mobile.pushnotification.constants.Preferences;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Created by lucasrodrigues on 10/3/16.
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
        return Route.EMAIL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();
            data.put(HttpBody.DEVICE_TOKEN, AllInPush.getInstance().getDeviceId());
            data.put(HttpBody.PLATFORM, Parameters.ANDROID);
            data.put(HttpBody.USER_EMAIL, this.email);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this.context);
        sharedPreferencesManager.storeData(Preferences.USER_EMAIL, this.email);

        return responseEntity.getMessage();
    }
}
