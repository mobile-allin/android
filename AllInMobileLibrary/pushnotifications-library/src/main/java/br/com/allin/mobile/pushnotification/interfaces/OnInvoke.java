package br.com.allin.mobile.pushnotification.interfaces;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.entity.ResponseEntity;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public interface OnInvoke<T> {
    String getUrl();

    String[] getParams();

    JSONObject getData();

    T onSuccess(ResponseEntity responseEntity);
}
