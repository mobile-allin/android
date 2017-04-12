package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for template campaign request
 */
public class TemplateTask extends BaseTask<String> {
    private int id;

    public TemplateTask(int id,
                        Context context, OnRequest onRequest) {
        super(context, RequestType.GET, false, onRequest);

        this.id = id;
    }

    @Override
    public String[] getParams() {
        return new String[] { String.valueOf(id) };
    }

    @Override
    public String getUrl() {
        return RouteConstants.CAMPAIGN;
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return  responseEntity.getMessage();
    }
}
