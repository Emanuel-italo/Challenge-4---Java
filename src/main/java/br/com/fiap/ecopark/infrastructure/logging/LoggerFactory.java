package br.com.fiap.ecopark.infrastructure.logging;


import br.com.fiap.ecopark.domain.logging.Logger;

public final class LoggerFactory {

    private LoggerFactory() {
        super();
    }

    public static Logger getLogger(Class<?> clazz) {
        return new LoggerImpl(clazz);
    }
}
