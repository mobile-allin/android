package br.com.allin.mobile.pushnotification.task;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstant;
import br.com.allin.mobile.pushnotification.constants.RouteConstant;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for device status request
 */
public class StatusTask extends BaseTask<Boolean> {
    public StatusTask(OnRequest onRequest) {
        super(RequestType.GET, false, onRequest);
    }

    @Override
    public String getUrl() {
        return RouteConstant.DEVICE_STATUS;
    }

    @Override
    public String[] getParams() {
        return new String[] { AlliNPush.getInstance().getDeviceToken() };
    }

    @Override
    public Boolean onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage().equalsIgnoreCase(HttpBodyConstant.ENABLED);
    }
}
