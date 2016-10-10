package br.com.allin.mobile.pushnotification.interfaces;

/**
 * @author lucasbrsilva
 *
 * Interface that returns the push action text
 *
 */
public interface OnActionNotification {
    /**
     * Método chamado que chhama ação do push
     *
     * @param action Ação do push
     */
    void onAction(String action);
}
