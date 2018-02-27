package br.com.allin.mobile.pushnotification.interfaces;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;

/**
 * Format request (standard All iN)
 */
public interface OnInvoke<T> {
    String getUrl();

    String[] getParams();

    JSONObject getData();

    T onSuccess(AIResponse AIResponse);
}