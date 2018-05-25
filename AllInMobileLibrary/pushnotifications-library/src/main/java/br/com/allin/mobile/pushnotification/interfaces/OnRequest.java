package br.com.allin.mobile.pushnotification.interfaces;

/**
 * <p>
 *     Listener of configuration.
 * <p>
 *     It is used to notify success and failure in the main asynchronous actions of the library.
 */
public interface OnRequest<T> {
    /**
     * Method called at the end of a given action.
     */
    void onFinish(T value);

    /**
     * Method called when there is an error in the execution of a particular action.
     *
     * @param exception Except that generated the error in action.
     */
    void onError(Exception exception);
}