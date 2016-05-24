package br.com.allin.mobile.pushnotification.exception;

/**
 * Exception raised when a mandatory attribute is null or passed as a string blank.
 */
public class NotNullAttributeOrPropertyException extends AbstractException {
    protected static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     *
     * @param attrParam Attribute or parameter with invalid value
     * @param clazzMethod Class or method with an invalid value
     */
    public NotNullAttributeOrPropertyException(String attrParam, String clazzMethod) {
        super("O atributo/parâmetro %s da classe/método %s é obrigatório, portanto, não pode ser nulo.", attrParam, clazzMethod);
    }

}
