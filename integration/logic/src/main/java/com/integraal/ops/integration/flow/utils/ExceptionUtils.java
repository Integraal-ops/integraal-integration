package com.integraal.ops.integration.flow.utils;

import com.integraal.ops.integration.model.persistence.FlowException;
import com.integraal.ops.integration.model.repositories.FlowExceptionsRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ExceptionUtils {

    public static UUID storeExceptionInFlowToTheDatabase(FlowExceptionsRepository flowExceptionsRepository, Exception exceptionToStore) {
        FlowException flowExceptions = FlowException.builder()
            .exceptionType(exceptionToStore.getClass().getCanonicalName())
            .message(exceptionToStore.getMessage())
            .stackTrace(getStackTraceAsString(exceptionToStore))
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        flowExceptions = flowExceptionsRepository.save(flowExceptions);
        return flowExceptions.getId();
    }


    private static String getStackTraceAsString(Exception exception) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
