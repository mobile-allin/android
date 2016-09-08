package br.com.allin.mobile.pushnotification.entity;

/**
 * Class with the return information from a request from the server.
 */
public class ResponseData {
    private boolean success;
    private String message;

    /**
     * Returns whether the request was successful.
     *
     * @return TRUE if the request was successful, otherwise FALSE
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets whether the request was successful or not.
     *
     * @param success TRUE/FALSE
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Return message from the WS.
     *
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Configure message sent by WS.
     *
     * @param message Error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
