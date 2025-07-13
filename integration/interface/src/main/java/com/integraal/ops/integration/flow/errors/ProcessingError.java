package com.integraal.ops.integration.flow.errors;

import com.integraal.ops.integration.storage.errors.StorageReadError;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;

public sealed interface ProcessingError permits
        ProcessingError.FatalProcessingError,
        ProcessingError.UserDataReadProcessingError,
        ProcessingError.UserDataWriteProcessingError
{
    Exception toException();

    // ? Errors Used in the Process / Flow control
    record UserDataReadProcessingError(StorageReadError readError) implements ProcessingError {
        @Override
        public Exception toException() {
            return null;
        }
    }
    record UserDataWriteProcessingError(StorageWriteError writeError) implements ProcessingError {
        @Override
        public Exception toException() {
            return new ServiceFatalException("Exception during writing userData", writeError.toException());
        }
    }

    // ? Errors used in the "User space"
    record FatalProcessingError(String message, Exception cause) implements ProcessingError {
        @Override
        public Exception toException() {
            return cause;
        }
    }
}
