package br.com.allin.mobile.pushnotification.exception;

/**
 * Base class for all library exceptions.
 */
public abstract class AbstractException extends Exception {

    /**
     * Error message that gave rise to the error (in the treatment of an exception or Java platform)
     */
    private String originalMessage = null;

    protected static final long serialVersionUID = 1L;

    /**
     * <p>
     * Default Constructor.
     * <br>
     * Filmography The exceptions may have 2 posts 1 AllIn (message), indicating what occurred,
     * and the default (original), which is the native platform error, server, or Java that originated this exception.
     * </p>
     * <p>
     * The parameters refer only Allin message being that should appear in the spring in the format: $ s.
     * </p>
     *
     * @param message Error message AllIn
     * @param params The error message parameters
     *
     */
    public AbstractException(String message, Object... params) {
        super(String.format(message, params));
    }
}
