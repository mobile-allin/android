package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for device status request
 */
public class StatusTask extends BaseTask<Boolean> {
    public StatusTask(Context context, OnRequest onRequest) {
        super(context, RequestType.GET, false, onRequest);
    }

    @Override
    public String getUrl() {
        return RouteConstants.DEVICE_STATUS;
    }

    @Override
    public String[] getParams() {
        return new String[] { AllInPush.getInstance().getDeviceId() };
    }

    @Override
    public Boolean onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage().equalsIgnoreCase(HttpBodyConstants.ENABLED);
    }
}
