package com.integraal.ops.integration.data.errors;

public sealed interface ReadDatabaseError permits ReadDatabaseError.FunctionalReadDatabaseError, ReadDatabaseError.TechnicalReadDatabaseError
{
    Exception toException();

    record FunctionalReadDatabaseError(
        String message,
        Exception e
    ) implements ReadDatabaseError {
        @Override
        public Exception toException() {
            return e;
        }
    }

    record TechnicalReadDatabaseError(
        String message,
        Exception e
    ) implements ReadDatabaseError {

        @Override
        public Exception toException() {
            return e;
        }
    }
}
