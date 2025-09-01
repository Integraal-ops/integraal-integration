package com.integraal.ops.integration.model.configuration;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplicationUuidGenerator implements UuidGenerator {
    @Override
    public UUID generateUuidForDB(Object record) {
        return UUID.randomUUID();
    }
}
