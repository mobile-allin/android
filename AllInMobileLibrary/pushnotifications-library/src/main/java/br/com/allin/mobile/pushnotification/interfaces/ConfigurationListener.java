package br.com.allin.mobile.pushnotification.interfaces;

/**
 * <p>
 *     Listener de configuração.
 * <p>
 *     É usado para notificar sucesso e falha nas principais ações assíncronas da biblioteca.
 */
public interface ConfigurationListener<T> {

    /**
     * Método chamado no fim de uma determinada ação.
     */
    void onFinish(T value);

    /**
     * Método chamado quando há algum erro na execução de uma determinada ação.
     * @param exception
     *          Exceção que gerou o erro na ação.
     */
    void onError(Exception exception);
}
