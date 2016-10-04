package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class NotificationCampaignTask extends BaseTask<String> {
    private int id;
    private double latitude;
    private double longitude;

    public NotificationCampaignTask(int id, Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        this.id = id;
        this.latitude = 0;
        this.longitude = 0;
    }

    public NotificationCampaignTask(int id, double latitude, double longitude,
                                    Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getUrl() {
        return Route.NOTIFICATION_CAMPAIGN;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBody.ID, id);
            data.put(HttpBody.DATE, Util.currentDate("yyyy-MM-dd HH:mm:ss"));
            data.put(HttpBody.DEVICE_TOKEN, AllInPush.getInstance().getDeviceId());

            if (this.latitude != 0 && this.longitude != 0) {
                data.put(HttpBody.LATITUDE, String.valueOf(this.latitude));
                data.put(HttpBody.LONGITUDE, String.valueOf(this.longitude));
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
