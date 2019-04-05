package br.com.allin.mobile.pushnotification.interfaces;

import android.content.Context;

/**
 * @author lucasbrsilva
 *
 * Interface that returns the push action text
 */
public interface AllInDelegate {
    void onSilentMessageReceived(String identifier);

    void onClickAction(String identifier);

    boolean onShowAlert(Context context, String title, String body, AlertCallback callback);
}