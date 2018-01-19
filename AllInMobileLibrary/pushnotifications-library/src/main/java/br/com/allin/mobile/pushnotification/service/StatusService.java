package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.StatusTask;
import br.com.allin.mobile.pushnotification.task.ToggleTask;

/**
 * Service class for device status in server
 */
public class StatusService {
    private Context context;
    private OnRequest onRequest;

    public StatusService(Context context, OnRequest onRequest) {
        this.context = context;
        this.onRequest = onRequest;
    }

    public StatusService(Context context) {
        this.context = context;
    }

    public void enable() {
        new ToggleTask(true, onRequest).execute();
    }

    public void disable() {
        new ToggleTask(false, onRequest).execute();
    }

    public void deviceIsEnable() {
        new StatusTask(onRequest).execute();
    }
}