package br.com.fiap.ecopark.infrastructure.logging;

import br.com.fiap.ecopark.domain.logging.Logger;
import org.slf4j.LoggerFactory;

final class LoggerImpl implements Logger {

    private final org.slf4j.Logger slf4jLogger;

    public LoggerImpl(Class<?> clazz) {
        this.slf4jLogger = LoggerFactory.getLogger(clazz);
    }


    @Override
    public void info(String message) {
        this.slf4jLogger.info(message);
    }

    @Override
    public void warn(String message) {
        this.slf4jLogger.warn(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        this.slf4jLogger.error(message, throwable);
    }

    @Override
    public void debug(String message) {
        this.slf4jLogger.debug(message);
    }
}
