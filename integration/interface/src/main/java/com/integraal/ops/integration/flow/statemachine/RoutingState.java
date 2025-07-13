package com.integraal.ops.integration.flow.statemachine;

import com.integraal.ops.integration.flow.beans.RoutingInBean;
import com.integraal.ops.integration.flow.beans.RoutingType;
import com.integraal.ops.integration.storage.beans.UserDataType;
import io.vavr.Tuple2;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public sealed interface RoutingState permits
    RoutingState.RoutingStateEntryPointDiscovery,
    RoutingState.RoutingStateEntryPointProcess,
    RoutingState.RoutingStateEntryPointIssue,
    RoutingState.RoutingStateInStepProcess,
    RoutingState.RoutingStateInStepIssue,
    RoutingState.RoutingStateInvalid
{

    // * Entrypoint routing
    record RoutingStateEntryPointDiscovery(
        Optional<UUID> flowId,
        Optional<Tuple2<UserDataType, UUID>> flowDataId,
        ZonedDateTime receivedDate
    ) implements RoutingState {}

    record RoutingStateEntryPointProcess(
        UUID flowKeyId,
        UUID flowId,
        Optional<Tuple2<UserDataType, UUID>> flowDataId,
        ZonedDateTime receivedDate
    ) implements RoutingState {}

    record RoutingStateEntryPointIssue(
        Optional<UUID> flowKeyId,
        UUID flowId,
        UUID exceptionId,
        Optional<Tuple2<UserDataType, UUID>> flowDataId,
        ZonedDateTime receivedDate
    ) implements RoutingState {}

    // * In Step routing
    record RoutingStateInStepProcess(
        UUID flowKeyId,
        UUID flowId,
        UUID originStepKeyId,
        UUID originStepId,
        Optional<Tuple2<UserDataType, UUID>> flowDataId,
        ZonedDateTime receivedDate
    ) implements RoutingState {}

    record RoutingStateInStepIssue(
        Optional<UUID> flowKeyId,
        UUID flowId,
        Optional<UUID> originStep,
        Optional<UUID> originStepKeyId,
        UUID exceptionId,
        Optional<Tuple2<UserDataType, UUID>> flowDataId,
        ZonedDateTime receivedDate
    ) implements RoutingState {}

    // * Unknown routing
    record RoutingStateInvalid(
        RoutingInBean routingInBean,
        String reason
    ) implements RoutingState {}

    @NonNull
    static RoutingState fromRoutingInbean(RoutingInBean routingInBean) {
        boolean isRoutingInBeanNullSafe = checkNullityOfRoutingFields(routingInBean);
        if (!isRoutingInBeanNullSafe) {
            return new RoutingStateInvalid(routingInBean, RoutingStateConstants.NULL_SAFETY_NOT_CHECKED);
        }
        RoutingType routingType = routingInBean.getRoutingType();
        return switch (routingType) {
            case DISCOVERY_ROUTING -> manageDiscoveryChecks(routingInBean);
            case STEP_ROUTING -> manageRoutingChecks(routingInBean);
            case ISSUE_ROUTING -> manageIssueChecks(routingInBean);
        };
    }

    private static RoutingState manageIssueChecks(RoutingInBean routingInBean) {
        if (routingInBean.getExceptionOnOriginStep().isEmpty()) {
            return new RoutingStateInvalid(routingInBean, RoutingStateConstants.ISSUE_WITHOUT_EXCEPTION_REGISTERED);
        }
        if (routingInBean.getFlowId().isEmpty()) {
            return new RoutingStateInvalid(routingInBean, RoutingStateConstants.ISSUE_WITHOUT_FLOW_ID);
        }
        if (routingInBean.getOriginStep().isEmpty()) {
            return new RoutingStateEntryPointIssue(
                routingInBean.getFlowKeyId(),
                routingInBean.getFlowId().get(),
                routingInBean.getExceptionOnOriginStep().get(),
                routingInBean.getFlowDataId(),
                routingInBean.getReceivedDate()
            );
        } else {
            return new RoutingStateInStepIssue(
                routingInBean.getFlowKeyId(),
                routingInBean.getFlowId().get(),
                routingInBean.getOriginStep(),
                routingInBean.getOriginStepKeyId(),
                routingInBean.getExceptionOnOriginStep().get(),
                routingInBean.getFlowDataId(),
                routingInBean.getReceivedDate()
            );
        }
    }

    private static RoutingState manageRoutingChecks(RoutingInBean routingInBean) {
        if (routingInBean.getFlowKeyId().isEmpty()) {
            return new RoutingStateInvalid(routingInBean, RoutingStateConstants.ROUTING_WITH_UNFILLED_FLOW_KEY_ID);
        }
        if (routingInBean.getExceptionOnOriginStep().isPresent()) {
            return new RoutingStateInvalid(routingInBean, RoutingStateConstants.ROUTING_WITH_EXCEPTION);
        }
        if (routingInBean.getFlowId().isEmpty()) {
            return new RoutingStateInvalid(routingInBean, RoutingStateConstants.ROUTING_WITH_UNFILLED_FLOW_ID);
        }
        // Routing for Entrypoint
        if (routingInBean.getOriginStep().isEmpty()) {
            if (routingInBean.getOriginStepKeyId().isPresent()) {
                return new RoutingStateInvalid(routingInBean, RoutingStateConstants.ROUTING_ENTRYPOINT_BUT_ORIGIN_STEP);
            }
            return new RoutingStateEntryPointProcess(
                routingInBean.getFlowKeyId().get(),
                routingInBean.getFlowId().get(),
                routingInBean.getFlowDataId(),
                routingInBean.getReceivedDate()
            );
        } else {
            // Routing for In Step Process
            if (routingInBean.getOriginStepKeyId().isEmpty()) {
                return new RoutingStateInvalid(routingInBean, RoutingStateConstants.ROUTING_INSTEP_BUT_ORIGIN_STEP);
            }
            return new RoutingStateInStepProcess(
                routingInBean.getFlowKeyId().get(),
                routingInBean.getFlowId().get(),
                routingInBean.getOriginStepKeyId().get(),
                routingInBean.getOriginStep().get(),
                routingInBean.getFlowDataId(),
                routingInBean.getReceivedDate()
            );
        }
    }

    private static RoutingState manageDiscoveryChecks(RoutingInBean routingInBean) {
        if (
            routingInBean.getExceptionOnOriginStep().isPresent()
            || routingInBean.getFlowKeyId().isPresent()
            || routingInBean.getOriginStep().isPresent()
            || routingInBean.getOriginStepKeyId().isPresent()
        ) {
            return new RoutingStateInvalid(routingInBean, RoutingStateConstants.DISCOVERY_PROCESS_WITH_EXCEPTION);
        }
        return new RoutingStateEntryPointDiscovery(
            routingInBean.getFlowId(),
            routingInBean.getFlowDataId(),
            routingInBean.getReceivedDate()
        );
    }

    // ! The comparison of Optional with null is done to prevent NPE on logic checks
    @SuppressWarnings(value = {"Optional value is compared with null"})
    private static boolean checkNullityOfRoutingFields(RoutingInBean routingInBean) {
        if (routingInBean == null) {
            return false;
        }
        //noinspection OptionalAssignedToNull
        return routingInBean.getFlowId() != null
            && routingInBean.getFlowKeyId() != null
            && routingInBean.getOriginStep() != null
            && routingInBean.getOriginStepKeyId() != null
            && routingInBean.getExceptionOnOriginStep() != null
            && routingInBean.getFlowDataId() != null
            && routingInBean.getReceivedDate() != null;
    }
}
