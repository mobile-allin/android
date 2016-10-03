package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class StatusService extends BaseService<Boolean> {
    public StatusService(Context context, OnRequest onRequest) {
        super(context, RequestType.GET, false, onRequest);
    }

    @Override
    public String getUrl() {
        return Route.DEVICE_STATUS;
    }

    @Override
    public String[] getParams() {
        return new String[] { AllInPush.getInstance().getDeviceId() };
    }

    @Override
    public Boolean onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage().equalsIgnoreCase(HttpBody.ENABLED);
    }
}
