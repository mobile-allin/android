package br.com.allin.mobile.pushnotification.task.allin;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.constants.HttpBodyIdentifier;
import br.com.allin.mobile.pushnotification.constants.Routes;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Thread for notification campaign request
 */
public class NotificationCampaignTask extends BaseTask<String> {
    private int id;
    private String date;
    private double latitude;
    private double longitude;

    public NotificationCampaignTask(int id, String date) {
        super(RequestType.POST, true, null);

        this.id = id;
        this.date = date;
        this.latitude = 0;
        this.longitude = 0;
    }

    public NotificationCampaignTask(int id, String date, double latitude, double longitude) {
        super(RequestType.POST, true, null);

        this.id = id;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + Routes.NOTIFICATION_CAMPAIGN;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyIdentifier.ID, this.id);
            data.put(HttpBodyIdentifier.DATE, this.date);
            data.put(HttpBodyIdentifier.DATE_OPENING, Util.currentDate("yyyy-MM-dd HH:mm:ss"));
            data.put(HttpBodyIdentifier.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());

            if (this.latitude != 0 && this.longitude != 0) {
                data.put(HttpBodyIdentifier.LATITUDE, String.valueOf(this.latitude));
                data.put(HttpBodyIdentifier.LONGITUDE, String.valueOf(this.longitude));
            }

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
