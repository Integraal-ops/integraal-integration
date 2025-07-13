package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.EntrypointFlowInbean;
import com.integraal.ops.integration.flow.beans.RoutingInBean;
import com.integraal.ops.integration.flow.beans.RoutingType;
import com.integraal.ops.integration.flow.errors.EntrypointError;
import com.integraal.ops.integration.storage.ExceptionStorageService;
import com.integraal.ops.integration.storage.UserDataStorageService;
import com.integraal.ops.integration.storage.beans.ExceptionStoreInBean;
import com.integraal.ops.integration.storage.beans.UserDataStorageType;
import com.integraal.ops.integration.storage.beans.UserDataStoreInBean;
import com.integraal.ops.integration.storage.beans.UserDataType;
import com.integraal.ops.integration.transversal.contexts.FlowMethodContext;
import io.vavr.Tuple;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class StandardEntrypointServiceImpl implements EntrypointService {
    private final MessageChannel routingChannel;
    private final UserDataStorageService userDataStorageService;
    private final ExceptionStorageService exceptionStorageService;

    @Autowired
    public StandardEntrypointServiceImpl(
            MessageChannel routingChannel,
            UserDataStorageService userDataStorageService,
            ExceptionStorageService exceptionStorageService
    ) {
        this.routingChannel = routingChannel;
        this.userDataStorageService = userDataStorageService;
        this.exceptionStorageService = exceptionStorageService;
    }

    @Override
    public void handleMessage(EntrypointFlowInbean entrypointFlowInbean) {
        log.info("Entrypoint for flow '{}' started", entrypointFlowInbean);
        // ? Flow iteration ID
        UUID flowId = UUID.randomUUID();

        FlowMethodContext initialContext = FlowMethodContext.initializeFlowMethodContext();
        FlowMethodContext resultContext = validateEntrypointFlowInbean(entrypointFlowInbean)
            .flatMap(StandardEntrypointServiceImpl::sanitizeData)
            .flatMap(
                efib -> buildRoutingInbeanFromEntrypoint(userDataStorageService, efib, flowId))
            .fold(
                error -> handleErrorEntrypointFlowInbean(exceptionStorageService, userDataStorageService, routingChannel, initialContext, entrypointFlowInbean, flowId),
                routingInBean -> handleSuccessEntrypointFlowInbean(routingChannel, initialContext, routingInBean)
            );

    }

    private static Either<EntrypointError, EntrypointFlowInbean> validateEntrypointFlowInbean(
        EntrypointFlowInbean entrypointFlowInbean
    ) {
        // ! For now, we trust all inputs for test
        // ! Add Bean validation process with a stateMachine definition (Some Type)
        return Either.right(entrypointFlowInbean);
    }

    private static Either<EntrypointError, EntrypointFlowInbean> sanitizeData(
        EntrypointFlowInbean entrypointFlowInbean
    ) {
        // ! For now, we trust all inputs for test
        return Either.right(entrypointFlowInbean);
    }

    private static Either<EntrypointError, RoutingInBean> buildRoutingInbeanFromEntrypoint(
        UserDataStorageService userDataStorageService,
        EntrypointFlowInbean entrypointFlowInbean,
        UUID flowId
    ) {
        Optional<UUID> flowKeyId = entrypointFlowInbean.getFlowKeyId();
        UserDataStoreInBean userDataStoreInBean = UserDataStoreInBean.builder()
            .userDataStorageType(UserDataStorageType.IN_MEMORY_STORAGE)
            .inputMetaData(entrypointFlowInbean.getInputMetaData())
            .inputData(entrypointFlowInbean.getInputData())
            .build();
        var storedUnsanitizedUserData = userDataStorageService.storeUnsanitizedUserData(userDataStoreInBean);
        if (storedUnsanitizedUserData.isLeft()) {
            return Either.left(new EntrypointError.UserDataPersistenceError(
                storedUnsanitizedUserData.getLeft()
            ));
        }
        UUID userDataId = storedUnsanitizedUserData.get().getStoredDataId();
        var userData = Optional.of(Tuple.of(UserDataType.UNSANITIZED, userDataId));
        if (flowKeyId.isPresent()) {
            return Either.right(
                RoutingInBean.builder()
                    .routingType(RoutingType.STEP_ROUTING)
                    .flowKeyId(flowKeyId)
                    .flowId(Optional.of(flowId))
                    .originStepKeyId(Optional.empty())
                    .originStep(Optional.empty())
                    .flowDataId(userData)
                    .exceptionOnOriginStep(Optional.empty())
                    .receivedDate(entrypointFlowInbean.getReceivedDate())
                    .build()
            );
        } else {
            return Either.right(
                RoutingInBean.builder()
                    .routingType(RoutingType.DISCOVERY_ROUTING)
                    .flowKeyId(flowKeyId)
                    .flowId(Optional.of(flowId))
                    .originStepKeyId(Optional.empty())
                    .originStep(Optional.empty())
                    .flowDataId(userData)
                    .exceptionOnOriginStep(Optional.empty())
                    .receivedDate(entrypointFlowInbean.getReceivedDate())
                    .build()
            );
        }
    }


    private static FlowMethodContext handleErrorEntrypointFlowInbean(
            // * Autowired lifecycle component
            ExceptionStorageService exceptionStorageService,
            UserDataStorageService userDataStorageService,
            MessageChannel routingChannel,
            // * Data used Component
            FlowMethodContext flowMethodContext,
            EntrypointFlowInbean entrypointFlowInbean,
            UUID flowId
    ) {
        Optional<UUID> flowKeyId = entrypointFlowInbean.getFlowKeyId();
        UserDataStoreInBean userDataStoreInBean = UserDataStoreInBean.builder()
            .userDataStorageType(UserDataStorageType.IN_MEMORY_STORAGE)
            .inputMetaData(entrypointFlowInbean.getInputMetaData())
            .inputData(entrypointFlowInbean.getInputData())
            .build();
        RoutingInBean routingInBean = userDataStorageService.storeUnsanitizedUserData(userDataStoreInBean)
            .fold(
                storageWriteError -> {
                    ExceptionStoreInBean exceptionStoreInBean = ExceptionStoreInBean.builder()
                        .exceptionToStore(storageWriteError.toException())
                        .build();
                    var errorStored = exceptionStorageService.storeExceptionData(exceptionStoreInBean)
                        .fold(
                            // ! TODO :: 22/06/2025 :: Do not ignore this error in the future
                            _ignored -> Optional.<UUID>empty(),
                            exceptionStoreOutBean -> Optional.of(exceptionStoreOutBean.getExceptionId())
                        );
                    return RoutingInBean.builder()
                        .routingType(RoutingType.ISSUE_ROUTING)
                        .flowKeyId(flowKeyId)
                        .flowId(Optional.of(flowId))
                        .originStepKeyId(Optional.empty())
                        .originStep(Optional.empty())
                        .flowDataId(Optional.empty())
                        .exceptionOnOriginStep(errorStored)
                        .receivedDate(entrypointFlowInbean.getReceivedDate())
                        .build();
                },
                userDataStoreOutBean -> RoutingInBean.builder()
                    .routingType(RoutingType.ISSUE_ROUTING)
                    .flowKeyId(flowKeyId)
                    .flowId(Optional.of(flowId))
                    .originStepKeyId(Optional.empty())
                    .originStep(Optional.empty())
                    .flowDataId(Optional.of(
                        Tuple.of(
                            UserDataType.UNSANITIZED,
                            userDataStoreOutBean.getStoredDataId()
                        )
                    ))
                    .exceptionOnOriginStep(Optional.empty())
                    .receivedDate(entrypointFlowInbean.getReceivedDate())
                    .build()
            );
        routingChannel.send(MessageBuilder.withPayload(routingInBean).build());
        return flowMethodContext;
    }

    private FlowMethodContext handleSuccessEntrypointFlowInbean(
            MessageChannel routingChannel,
            FlowMethodContext flowMethodContext,
            RoutingInBean routingInBean
    ) {
        // ! TODO :: Add Context Data for Metrics / Logs / Spans
        routingChannel.send(MessageBuilder.withPayload(routingInBean).build());
        return flowMethodContext;
    }
}
