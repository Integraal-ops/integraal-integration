package com.integraal.ops.integration.model.configuration;

import java.util.UUID;

// The purpose of UuidGenerator is to have a predictive uuid generator for tests assertions
public interface UuidGenerator {
    UUID generateUuidForDB(Object record);
}
