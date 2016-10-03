package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.Parameters;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.DeviceTask;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class DeviceService {
    public void sendDeviceInfo(final Context context,
                               final DeviceEntity deviceEntity, final OnRequest onRequest) {
        new DeviceTask(deviceEntity, context, new OnRequest() {
            @Override
            public void onFinish(Object value) {
                String pushId = AllInPush.getInstance().getDeviceId();
                Map<String, String> map = new HashMap<>();
                map.put("id_push", Util.md5(pushId));
                map.put("push_id", pushId);
                map.put("plataforma", Parameters.ANDROID);


                AllInPush.getInstance().sendList("Lista Padrao Push", map, onRequest);
            }

            @Override
            public void onError(Exception exception) {

            }
        }).execute();
    }
}
