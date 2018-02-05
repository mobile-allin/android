package br.com.allin.mobile.pushnotification.task;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstant;
import br.com.allin.mobile.pushnotification.constants.RouteConstant;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;

/**
 * Thread for notification transactional request
 */
public class NotificationTransactionalTask extends BaseTask<String> {
    private int id;
    private String date;

    public NotificationTransactionalTask(int id, String date) {
        super(RequestType.POST, true, null);

        this.id = id;
        this.date = date;
    }

    @Override
    public String getUrl() {
        return RouteConstant.NOTIFICATION_TRANSACTIONAL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyConstant.ID, this.id);
            data.put(HttpBodyConstant.DATE, this.date);
            data.put(HttpBodyConstant.DATE_OPENING, Util.currentDate("yyyy-MM-dd HH:mm:ss"));

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
