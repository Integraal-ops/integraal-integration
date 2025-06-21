package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import com.integraal.ops.integration.transversal.services.LogicService;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.UUID;

public interface StepMappingService extends LogicService {

    MessageChannel getChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException;

    List<UUID> getNextStepsIdInFlow(UUID flowId, UUID stepId) throws ServiceFatalException;

    MessageChannel getMessageChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException;

    MessageChannel getIssueChannel();

    UUID getUnknownFlowId();
}
