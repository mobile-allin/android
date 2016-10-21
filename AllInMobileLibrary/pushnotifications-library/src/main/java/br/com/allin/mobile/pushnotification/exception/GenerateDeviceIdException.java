package br.com.allin.mobile.pushnotification.exception;

/**
 * Class responsible for exceptions related to creation of the token device.
 */
public class GenerateDeviceIdException extends AbstractException {
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
     * @param error Error parameters.
     */
    public GenerateDeviceIdException(String message, String error) {
        super("Device Id inválido: %s %s", message, error);
    }
}
