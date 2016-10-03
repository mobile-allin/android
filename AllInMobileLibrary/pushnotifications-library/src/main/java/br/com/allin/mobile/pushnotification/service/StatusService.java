package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.StatusTask;
import br.com.allin.mobile.pushnotification.task.ToggleTask;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class StatusService {
    private Context context;
    private OnRequest onRequest;

    public StatusService(Context context, OnRequest onRequest) {
        this.context = context;
        this.onRequest = onRequest;
    }

    public void enable() {
        new ToggleTask(true, this.context, this.onRequest).execute();
    }

    public void disable() {
        new ToggleTask(false, this.context, this.onRequest).execute();
    }

    public void deviceIsEnable() {
        new StatusTask(this.context, this.onRequest).execute();
    }
}
