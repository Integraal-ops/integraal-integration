package com.integraal.ops.integration.data.errors;

public sealed interface WriteDatabaseError
permits
    WriteDatabaseError.FunctionalWriteDatabaseError,
    WriteDatabaseError.TechnicalWriteDatabaseError
{
    Exception toException();

    record FunctionalWriteDatabaseError(
        String message,
        Exception e
    ) implements WriteDatabaseError {
        @Override
        public Exception toException() {
            return e;
        }
    }

    record TechnicalWriteDatabaseError(
        String message,
        Exception e
    ) implements WriteDatabaseError {

        @Override
        public Exception toException() {
            return e;
        }
    }
}
