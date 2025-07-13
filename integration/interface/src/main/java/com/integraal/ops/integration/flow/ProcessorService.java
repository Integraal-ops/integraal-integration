package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.FlowStepInbean;
import com.integraal.ops.integration.flow.beans.ProcessorInbean;
import com.integraal.ops.integration.flow.beans.ProcessorOutbean;
import com.integraal.ops.integration.flow.beans.RoutingInBean;
import com.integraal.ops.integration.flow.beans.RoutingType;
import com.integraal.ops.integration.flow.errors.ProcessingError;
import com.integraal.ops.integration.storage.ExceptionStorageService;
import com.integraal.ops.integration.storage.UserDataStorageService;
import com.integraal.ops.integration.storage.beans.ExceptionStoreInBean;
import com.integraal.ops.integration.storage.beans.UserDataRetrieveInBean;
import com.integraal.ops.integration.storage.beans.UserDataRetrieveOutBean;
import com.integraal.ops.integration.storage.beans.UserDataStorageType;
import com.integraal.ops.integration.storage.beans.UserDataStoreInBean;
import com.integraal.ops.integration.storage.beans.UserDataStoreOutBean;
import com.integraal.ops.integration.storage.beans.UserDataType;
import com.integraal.ops.integration.storage.errors.StorageReadError;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import com.integraal.ops.integration.transversal.contexts.FlowMethodContext;
import com.integraal.ops.integration.transversal.services.LogicService;
import io.vavr.Tuple;
import io.vavr.control.Either;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.function.ThrowingFunction;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface ProcessorService extends LogicService {

    MessageChannel getRoutingChannel();
    UserDataStorageService getUserDataStorageService();
    ExceptionStorageService getExceptionStorageService();
    Either<ProcessingError, ProcessorOutbean> processMessage(ProcessorInbean inbean) throws Exception;

    default void handleMessageWithRoutingAndIssue(FlowStepInbean flowStepInbean) {
        MessageChannel routingChannel = getRoutingChannel();
        UserDataStorageService userDataStorageService = getUserDataStorageService();
        ExceptionStorageService exceptionStorageService = getExceptionStorageService();
        UUID flowId = flowStepInbean.getFlowId();
        UUID flowKeyId = flowStepInbean.getFlowKeyId();
        UUID stepId = flowStepInbean.getStepId();
        UUID stepKeyId = flowStepInbean.getStepKeyId();
        Optional<UUID> dataId = flowStepInbean.getFlowDataId();
        // ! This field is stateful for now, in the future It should be immutable and considered as such
        FlowMethodContext initialFlowMethodContext = FlowMethodContext.initializeFlowMethodContext();
        UserDataRetrieveInBean userDataRetrieveInBean = UserDataRetrieveInBean.builder()
            // ! Insecure use of Optional::get
            .storedDataId(dataId.get())
            // ! TODO :: 22/06/2025 :: Handle different Storage type at Flow/StepLevel
            .storageType(UserDataStorageType.IN_MEMORY_STORAGE)
            .build();

        FlowMethodContext flowMethodContext = userDataStorageService.retrieveUnsanitizedStoredUserData(userDataRetrieveInBean)
            // * Map the Left / Right due to type compliance
            .map(userDataRetrieveOutBean -> toProcessorInbean(userDataRetrieveOutBean, initialFlowMethodContext))
            .mapLeft(ProcessorService::fromUserStorageReadErrorToProcessorError)
            // * Process the message
            .flatMap(processorInbean -> processMessageWrapped(this::processMessage, processorInbean))
            // * Handle Post Process Flow
            .flatMap(processorOutbean -> registerResponseAsData(userDataStorageService, processorOutbean))
            .map(storedData -> ProcessorService.buildRoutingInBeanForNextStep(
                flowId, flowKeyId, stepId, stepKeyId, storedData
            ))
            .fold(
                processingError -> ProcessorService.handleProcessingError(
                    routingChannel, exceptionStorageService,
                    flowId, flowKeyId, stepId, stepKeyId,
                    dataId, processingError
                ),
                routingInbean -> ProcessorService.sendToNextStep(routingChannel, routingInbean)
            )
        ;
        // ! TODO :: register context after method
    }

    private static FlowMethodContext handleProcessingError(
            MessageChannel routingChannel,
            ExceptionStorageService exceptionStorageService,
            UUID flowId, UUID flowKeyId, UUID stepId, UUID stepKeyId,
            Optional<UUID> storedDataId,
            ProcessingError processingError
    ) {
        var flowDataId = storedDataId.map(id -> Tuple.of(UserDataType.UNSANITIZED, id));
        ExceptionStoreInBean exceptionStoreInBean = ExceptionStoreInBean.builder()
            .exceptionToStore(processingError.toException())
            .build();
        var errorStored = exceptionStorageService.storeExceptionData(exceptionStoreInBean)
            .fold(
                // ! TODO :: 22/06/2025 :: Do not ignore this error in the future
                _ignored -> Optional.<UUID>empty(),
                exceptionStoreOutBean -> Optional.of(exceptionStoreOutBean.getExceptionId())
            );
        RoutingInBean routingInbean = RoutingInBean.builder()
            .routingType(RoutingType.ISSUE_ROUTING)
            .flowKeyId(Optional.of(flowKeyId))
            .flowId(Optional.of(flowId))
            .originStep(Optional.of(stepId))
            .originStepKeyId(Optional.of(stepKeyId))
            .flowDataId(flowDataId)
            .exceptionOnOriginStep(errorStored)
            .build();
        routingChannel.send(MessageBuilder.withPayload(routingInbean).build());
        return null;
    }

    private static FlowMethodContext sendToNextStep(MessageChannel routingChannel, RoutingInBean routingInbean) {
        routingChannel.send(MessageBuilder.withPayload(routingInbean).build());
        return null;
    }

    private static RoutingInBean buildRoutingInBeanForNextStep(
            UUID flowId, UUID flowKeyId, UUID stepId, UUID stepKeyId,
            UserDataStoreOutBean userDataStoreOutBean
    ) {
        var userDataStored = Optional.of(Tuple.of(
            UserDataType.UNSANITIZED,
            userDataStoreOutBean.getStoredDataId()
        ));
        return RoutingInBean.builder()
            .routingType(RoutingType.STEP_ROUTING)
            .flowKeyId(Optional.of(flowKeyId))
            .flowId(Optional.of(flowId))
            .originStep(Optional.of(stepId))
            .originStepKeyId(Optional.of(stepKeyId))
            .flowDataId(userDataStored)
            .exceptionOnOriginStep(Optional.empty())
            .build();
    }

    private static Either<ProcessingError, UserDataStoreOutBean> registerResponseAsData(
        UserDataStorageService userDataStorageService,
        ProcessorOutbean processorOutbean
    ) {
        UserDataStoreInBean userDataStoreInBean = UserDataStoreInBean.builder()
            // ! TODO :: 22/06/2025 :: Handle different Storage type at Flow/StepLevel
            .userDataStorageType(UserDataStorageType.IN_MEMORY_STORAGE)
            .inputData(processorOutbean.getInputData())
            .inputMetaData(processorOutbean.getInputMetaData())
            .build();
        return userDataStorageService.storeUnsanitizedUserData(userDataStoreInBean)
            // * Map the Left / Right due to type compliance
            .mapLeft(ProcessorService::fromUserStorageWriteErrorToProcessorError);
    }

    private static Either<ProcessingError, ProcessorOutbean> processMessageWrapped(
            ThrowingFunction<ProcessorInbean, Either<ProcessingError, ProcessorOutbean>> processMethod,
            ProcessorInbean inbean
    ) {
        try {
            return processMethod.apply(inbean);
        } catch (Exception e) {
            return Either.left(new ProcessingError.FatalProcessingError("ProcessorService - processMessageWrapped - Exception during 'user-code' processing message", e));
        }
    }

    // * =========================== Type Mappers ===========================
    private static ProcessingError fromUserStorageReadErrorToProcessorError(StorageReadError storageReadError) {
        return new ProcessingError.UserDataReadProcessingError(storageReadError);
    }

    private static ProcessingError fromUserStorageWriteErrorToProcessorError(StorageWriteError storageWriteError) {
        return new ProcessingError.UserDataWriteProcessingError(storageWriteError);
    }

    private static ProcessorInbean toProcessorInbean(
            UserDataRetrieveOutBean userDataRetrieveOutBean,
            FlowMethodContext initialFlowMethodContext
    ) {
        return ProcessorInbean.builder()
            .inputData(userDataRetrieveOutBean.getInputData())
            .inputMetaData(userDataRetrieveOutBean.getInputMetaData())
            .flowMethodContext(initialFlowMethodContext)
            .build();
    }
}
