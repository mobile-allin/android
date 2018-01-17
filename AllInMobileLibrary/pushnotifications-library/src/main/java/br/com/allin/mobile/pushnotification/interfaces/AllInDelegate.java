package br.com.allin.mobile.pushnotification.interfaces;

import android.content.Context;

/**
 * @author lucasbrsilva
 *
 * Interface that returns the push action text
 */
public interface AllInDelegate {
    /**
     * Method call that calls the push action
     *
     * @param action Push action
     * @param sentFromServer Informs if action comes directly from the server or from push actions
     */
    void onAction(String action, boolean sentFromServer);
}
