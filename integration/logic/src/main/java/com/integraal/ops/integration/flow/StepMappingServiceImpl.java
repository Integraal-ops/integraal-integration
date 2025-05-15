package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StepMappingServiceImpl implements StepMappingService {



    @Override
    public MessageChannel getChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException {
        return null;
    }

    @Override
    public List<UUID> getNextStepsIdInFlow(UUID flowId, UUID stepId) throws ServiceFatalException {
        return List.of();
    }

    @Override
    public MessageChannel getMessageChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException {
        return null;
    }

    @Override
    public MessageChannel getIssueChannel() {
        return null;
    }

    @Override
    public UUID getUnknownFlowId() {
        return FlowConfigurationServiceImpl.UNKNOWN_FLOW_ID;
    }

}
