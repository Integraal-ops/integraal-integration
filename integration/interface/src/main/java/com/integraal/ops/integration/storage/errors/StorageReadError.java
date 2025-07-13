package com.integraal.ops.integration.storage.errors;

public interface StorageReadError {
    record UserStorageDataNotFoundReadError(String message) implements StorageReadError {}
}
