package br.com.allin.mobile.pushnotification.task.btg;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.entity.allin.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class TrackingTask extends BaseTask<String> {
    private JSONObject jsonObject;

    public TrackingTask(JSONObject jsonObject) {
        super(RequestType.POST, false, null);

        this.jsonObject = jsonObject;
    }

    @Override
    public JSONObject getData() {
        return jsonObject;
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_BTG;
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
