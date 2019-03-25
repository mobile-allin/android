package br.com.allin.mobile.pushnotification.task.allin;

import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.http.RequestType;
import br.com.allin.mobile.pushnotification.http.Routes;
import br.com.allin.mobile.pushnotification.task.BaseTask;

public class MessageTask extends BaseTask<String> {
    private int notificationId;

    public MessageTask(int notificationId) {
        super(RequestType.POST, true, null);

        this.notificationId = notificationId;
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + Routes.NOTIFICATION_MARK_AS_READ;
    }

    @Override
    public String[] getParams() {
        return new String[] { String.valueOf(this.notificationId) };
    }

    @Override
    public String onSuccess(AIResponse response) {
        return response.getMessage();
    }
}
