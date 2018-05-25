package br.com.allin.mobile.pushnotification.task.allin;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.identifiers.SystemIdentifier;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.identifiers.HttpBodyIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;
import br.com.allin.mobile.pushnotification.http.Routes;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.http.RequestType;
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
        return HttpConstant.URL_ALLIN + Routes.EMAIL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();
            data.put(HttpBodyIdentifier.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());
            data.put(HttpBodyIdentifier.PLATFORM, SystemIdentifier.ANDROID);
            data.put(HttpBodyIdentifier.USER_EMAIL, email);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(AIResponse AIResponse) {
        Context context = AlliNPush.getInstance().getContext();
        PreferencesManager preferencesManager = new PreferencesManager(context);
        preferencesManager.storeData(PreferenceIdentifier.USER_EMAIL, email);

        return AIResponse.getMessage();
    }
}
