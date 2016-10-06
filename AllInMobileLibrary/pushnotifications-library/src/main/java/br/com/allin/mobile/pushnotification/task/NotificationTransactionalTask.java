package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;

/**
 * Created by lucasrodrigues on 10/3/16.
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
        return Route.NOTIFICATION_TRANSACTIONAL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBody.ID, this.id);
            data.put(HttpBody.DATE, this.date);
            data.put(HttpBody.DATE_OPENING, Util.currentDate("yyyy-MM-dd HH:mm:ss"));

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
