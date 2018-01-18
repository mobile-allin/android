package br.com.allin.mobile.pushnotification.task;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;

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
        return RouteConstants.NOTIFICATION_CAMPAIGN;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyConstants.ID, this.id);
            data.put(HttpBodyConstants.DATE, this.date);
            data.put(HttpBodyConstants.DATE_OPENING, Util.currentDate("yyyy-MM-dd HH:mm:ss"));
            data.put(HttpBodyConstants.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());

            if (this.latitude != 0 && this.longitude != 0) {
                data.put(HttpBodyConstants.LATITUDE, String.valueOf(this.latitude));
                data.put(HttpBodyConstants.LONGITUDE, String.valueOf(this.longitude));
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
