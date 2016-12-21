package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;

/**
 * Thread for notification transactional request
 */
public class NotificationTransactionalTask extends BaseTask<String> {
    private int id;
    private String date;

    public NotificationTransactionalTask(int id, String date, Context context) {
        super(context, RequestType.POST, true, null);

        this.id = id;
        this.date = date;
    }

    @Override
    public String getUrl() {
        return RouteConstants.NOTIFICATION_TRANSACTIONAL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyConstants.ID, this.id);
            data.put(HttpBodyConstants.DATE, this.date);
            data.put(HttpBodyConstants.DATE_OPENING, Util.currentDate("yyyy-MM-dd HH:mm:ss"));

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
