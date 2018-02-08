package br.com.allin.mobile.pushnotification.exception;

/**
   Error thrown when an error occurred on the server (server message wrapper)
 */
public class WebServiceException extends AbstractException {
    protected static final long serialVersionUID = 1L;

    /**
       Default constructor.

       @param serverMessage Message sent by server
     */
    public WebServiceException(String serverMessage) {
        super("An error occurred while running the web service: %s", serverMessage);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
