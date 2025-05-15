package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.UUID;

public interface StepMappingService {

    MessageChannel getChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException;

    List<UUID> getNextStepsIdInFlow(UUID flowId, UUID stepId) throws ServiceFatalException;

    MessageChannel getMessageChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException;

    MessageChannel getIssueChannel();

    UUID getUnknownFlowId();
}
