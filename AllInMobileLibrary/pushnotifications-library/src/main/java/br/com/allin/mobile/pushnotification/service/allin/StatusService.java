package br.com.allin.mobile.pushnotification.service.allin;

import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.allin.StatusTask;
import br.com.allin.mobile.pushnotification.task.allin.ToggleTask;

/**
 * Service class for device status in server
 */
public class StatusService {
    private OnRequest onRequest;

    public StatusService() {
    }

    public StatusService(OnRequest onRequest) {
        this.onRequest = onRequest;
    }

    public void enable() {
        new ToggleTask(true, this.onRequest).execute();
    }

    public void disable() {
        new ToggleTask(false, this.onRequest).execute();
    }

    public void deviceIsEnable() {
        new StatusTask(this.onRequest).execute();
    }
}