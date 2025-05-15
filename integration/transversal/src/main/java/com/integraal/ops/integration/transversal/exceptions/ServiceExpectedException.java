package com.integraal.ops.integration.transversal.exceptions;

public class ServiceExpectedException extends Exception {
    public ServiceExpectedException(String message) {
        super(message);
    }

    public ServiceExpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
