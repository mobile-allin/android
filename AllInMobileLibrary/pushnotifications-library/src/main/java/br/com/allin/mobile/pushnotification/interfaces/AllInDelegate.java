package br.com.allin.mobile.pushnotification.interfaces;

/**
 * @author lucasbrsilva
 *
 * Interface that returns the push action text
 */
public interface AllInDelegate {
    void onSilentMessageReceived(String identifier);

    void onClickPush();

    void onClickAction(String identifier);
}