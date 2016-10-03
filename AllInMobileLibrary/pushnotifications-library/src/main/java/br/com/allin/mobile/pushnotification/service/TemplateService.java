package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class TemplateService extends BaseService<String> {
    private int id;

    public TemplateService(int id,
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
        return Route.CAMPAIGN;
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return  responseEntity.getMessage();
    }
}
