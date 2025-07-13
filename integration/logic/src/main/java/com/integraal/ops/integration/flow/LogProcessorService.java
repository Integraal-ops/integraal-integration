package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.ProcessorInbean;
import com.integraal.ops.integration.flow.beans.ProcessorOutbean;
import com.integraal.ops.integration.flow.errors.ProcessingError;
import com.integraal.ops.integration.storage.ExceptionStorageService;
import com.integraal.ops.integration.storage.UserDataStorageService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LogProcessorService implements ProcessorService {
    private final MessageChannel routingChannel;
    private final UserDataStorageService userDataStorageService;
    private final ExceptionStorageService exceptionStorageService;

    public LogProcessorService(
        MessageChannel routingChannel,
        UserDataStorageService userDataStorageService,
        ExceptionStorageService exceptionStorageService
    ) {
        this.routingChannel = routingChannel;
        this.userDataStorageService = userDataStorageService;
        this.exceptionStorageService = exceptionStorageService;
    }

    @Override
    public MessageChannel getRoutingChannel() {
        log.info("LogProcessorService - getRoutingChannel - Init Step");
        return routingChannel;
    }

    @Override
    public UserDataStorageService getUserDataStorageService() {
        log.info("LogProcessorService - getUserDataStorageService - Init Step");
        return userDataStorageService;
    }

    @Override
    public ExceptionStorageService getExceptionStorageService() {
        log.info("LogProcessorService - getExceptionStorageService - Init Step");
        return exceptionStorageService;
    }

    @Override
    public Either<ProcessingError, ProcessorOutbean> processMessage(ProcessorInbean inbean) throws Exception {
        log.info("Processing flow message with data: '{}'", inbean);
        ProcessorOutbean result = ProcessorOutbean.builder()
                .inputData(inbean.getInputData())
                .inputMetaData(inbean.getInputMetaData())
                .flowMethodContext(inbean.getFlowMethodContext())
                .build();
        return Either.right(result);
    }
}
