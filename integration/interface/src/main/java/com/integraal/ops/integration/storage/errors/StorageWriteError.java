package com.integraal.ops.integration.storage.errors;

public sealed interface StorageWriteError permits
        StorageWriteError.FatalSystemStorageWriteError
{
    Exception toException();
    public record FatalSystemStorageWriteError(Exception e) implements StorageWriteError {
        @Override
        public Exception toException() {
            return e;
        }
    }
}
