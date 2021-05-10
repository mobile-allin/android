package br.com.allin.mobile.pushnotification.task.allin;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.http.RequestType;
import br.com.allin.mobile.pushnotification.http.Routes;
import br.com.allin.mobile.pushnotification.identifiers.HttpBodyIdentifier;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Thread for notification transactional request
 */
public class NotificationTransactionalTask extends BaseTask<String> {
    private final String id;
    private final String date;

    public NotificationTransactionalTask(String id, String date) {
        super(RequestType.POST, true, null);

        this.id = id;
        this.date = date;
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + Routes.NOTIFICATION_TRANSACTIONAL;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyIdentifier.ID, this.id);
            data.put(HttpBodyIdentifier.DATE, this.date);
            data.put(HttpBodyIdentifier.DATE_OPENING, Util.currentDate("yyyy-MM-dd HH:mm:ss"));
            data.put(HttpBodyIdentifier.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceToken());

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(AIResponse AIResponse) {
        return AIResponse.getMessage();
    }
}
