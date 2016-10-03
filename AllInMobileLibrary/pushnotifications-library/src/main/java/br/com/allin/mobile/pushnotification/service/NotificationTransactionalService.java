package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class NotificationTransactionalService extends BaseService<String> {
    private int id;

    public NotificationTransactionalService(int id, Context context,
                                            OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        this.id = id;
    }

    @Override
    public String getUrl() {
        return Route.NOTIFICATION_TRANSACTIONAL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBody.ID, id);
            data.put(HttpBody.DATE, Util.currentDate("yyyy-MM-dd HH:mm:ss"));

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
