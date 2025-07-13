package com.integraal.ops.integration.flow.errors;

import com.integraal.ops.integration.storage.errors.StorageWriteError;

public sealed interface EntrypointError permits
    EntrypointError.NonValidEntrypointInputDataError,
    EntrypointError.UserDataPersistenceError
{
    record NonValidEntrypointInputDataError() implements EntrypointError {}
    record UserDataPersistenceError(StorageWriteError error) implements EntrypointError {}
}
