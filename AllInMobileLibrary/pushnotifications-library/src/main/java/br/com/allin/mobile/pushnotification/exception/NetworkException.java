package br.com.allin.mobile.pushnotification.exception;

/**
 * Exception class responsible for connectivity errors.
 */
public class NetworkException extends AbstractException {
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
     * @param params Error parameters.
     */
    public NetworkException(String message, Object... params) {
        super("Erro de conexão", message, params);
    }
}
