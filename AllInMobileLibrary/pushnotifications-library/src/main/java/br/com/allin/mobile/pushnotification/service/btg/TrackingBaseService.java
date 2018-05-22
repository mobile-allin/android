package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.task.btg.TrackingTask;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public abstract class TrackingBaseService<T> {
    private String account;
    private String event;
    private List<T> list;

    TrackingBaseService(String account, String event, List<T> list) {
        this.account = account;
        this.event = event;
        this.list = list;
    }

    public void send() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", account);
            jsonObject.put("event", event);
            jsonObject.put("deviceId", AlliNPush.getInstance().getIdentifier());
            jsonObject.put("deviceToken", AlliNPush.getInstance().getDeviceToken());
            jsonObject.put("items", transform(list));
            jsonObject.put("plataformId", 1);

            new TrackingTask(jsonObject).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract JSONArray transform(List<T> list);
}
