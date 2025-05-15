package com.integraal.ops.integration.transversal.exceptions;

public class ServiceFatalException extends RuntimeException {
    public ServiceFatalException(String message) {
        super(message);
    }

    public ServiceFatalException(String message, Throwable cause) {
        super(message, cause);
    }
}
