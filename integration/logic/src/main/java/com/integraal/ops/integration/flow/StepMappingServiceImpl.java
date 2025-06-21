package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StepMappingServiceImpl implements StepMappingService {
    private final GenericApplicationContext applicationContext;

    @Autowired
    public StepMappingServiceImpl(
        GenericApplicationContext applicationContext
    ) {
        this.applicationContext = applicationContext;
    }

    @Override
    public MessageChannel getChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException {
        FlowConfigurationService flowConfigurationService = applicationContext.getBean(FlowConfigurationService.class);
        return flowConfigurationService.getMessageChannel(flowId, stepId).orElseThrow();
    }

    @Override
    public List<UUID> getNextStepsIdInFlow(UUID flowId, UUID stepId) throws ServiceFatalException {
        return List.of();
    }

    @Override
    public MessageChannel getMessageChannelForStepId(UUID flowId, UUID stepId) throws ServiceFatalException {
        FlowConfigurationService flowConfigurationService = applicationContext.getBean(FlowConfigurationService.class);
        return flowConfigurationService.getMessageChannel(flowId, stepId).orElseThrow();
    }

    @Override
    public MessageChannel getIssueChannel() {
        return this.applicationContext.getBean("issueChannel", MessageChannel.class);
    }

    @Override
    public UUID getUnknownFlowId() {
        return FlowConfigurationServiceImpl.UNKNOWN_FLOW_ID;
    }

}
