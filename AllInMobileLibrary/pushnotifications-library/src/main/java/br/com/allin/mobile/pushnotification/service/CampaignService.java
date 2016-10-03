package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.TemplateTask;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class CampaignService {
    private Context context;
    private OnRequest onRequest;

    public CampaignService(Context context, OnRequest onRequest) {
        this.context = context;
        this.onRequest = onRequest;
    }

    public void getTemplate(int idCampaign) {
        new TemplateTask(idCampaign, context, onRequest).execute();
    }
}
