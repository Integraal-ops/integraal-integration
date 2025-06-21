package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.FlowStepInbean;
import com.integraal.ops.integration.flow.beans.IssueHandlerInbean;
import com.integraal.ops.integration.flow.beans.RoutingInBean;
import com.integraal.ops.integration.flow.utils.ExceptionUtils;
import com.integraal.ops.integration.model.persistence.ProcessStep;
import com.integraal.ops.integration.model.persistence.StepStatus;
import com.integraal.ops.integration.model.repositories.FlowExceptionsRepository;
import com.integraal.ops.integration.model.repositories.ProcessStepRepository;
import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RoutingServiceImpl implements RoutingService {

    private final StepMappingService stepMappingService;
    private final ProcessStepRepository processStepRepository;
    private final FlowExceptionsRepository flowExceptionsRepository;

    @Autowired
    public RoutingServiceImpl(
        StepMappingService stepMappingService,
        ProcessStepRepository processStepRepository,
        FlowExceptionsRepository flowExceptionsRepository
    ) {
        this.stepMappingService = stepMappingService;
        this.processStepRepository = processStepRepository;
        this.flowExceptionsRepository = flowExceptionsRepository;
    }

    @Override
    public void handleMessage(RoutingInBean routingInBean) {
        try {
            log.info("Handling flow steps in bean: '{}'", routingInBean);
            UUID flowId = routingInBean.getFlowId()
                .orElseThrow(() -> new ServiceFatalException("RoutingServiceImpl - handleMessage - No flowId for message. routingInBean '" + routingInBean + "'"));
            UUID flowKeyId = routingInBean.getFlowKeyId();
            UUID originStepId = routingInBean.getOriginStep();
            UUID originStepKeyId = routingInBean.getOriginStepKeyId();
            String flowDataId = routingInBean.getFlowDataId();
            MessageChannel issueChannel = this.stepMappingService.getIssueChannel();
            ProcessStep previousProcessStep = this.processStepRepository.findById(originStepKeyId)
                .orElseThrow();
            if (routingInBean.getExceptionOnOriginStep().isPresent()) {
                // TODO :: 17/08/2024 :: Implement Errors case : (Fatal Error + Warning Error)
                UUID exceptionId = routingInBean.getExceptionOnOriginStep().get();
                previousProcessStep.setEndTime(ZonedDateTime.now());
                previousProcessStep.setStepStatus(StepStatus.FAILED);
                previousProcessStep.setExceptionId(exceptionId);
                this.processStepRepository.saveAndFlush(previousProcessStep);
                sendIssueMessage(exceptionId, flowId, flowKeyId, originStepId, issueChannel, Boolean.TRUE);
            } else {
                terminateProcessAsCompleteSuccess(previousProcessStep, this.processStepRepository);
            }

            List<UUID> nextStepsId = this.stepMappingService.getNextStepsIdInFlow(flowId, originStepId);

            for (UUID nextStepId : nextStepsId) {
                // Build same message for all nextSteps
                FlowStepInbean flowStepInbean = buildNextMessageAndSaveProcess(nextStepId, flowId, flowKeyId, flowDataId, processStepRepository);
                Message<FlowStepInbean> messageToSend = MessageBuilder.withPayload(flowStepInbean).build();
                MessageChannel messageChannel = this.stepMappingService.getMessageChannelForStepId(flowId, nextStepId);
                boolean messageSent = messageChannel.send(messageToSend);
                if (!messageSent) {
                    Exception sendingMessageException = new ServiceFatalException("RoutingServiceImpl - handleMessage - Could not send message to next Step. flowStepInbean '" + flowStepInbean + "'");
                    sendIssueMessage(this.flowExceptionsRepository, sendingMessageException, flowId, flowKeyId, originStepId, issueChannel, Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            MessageChannel issueChannel = this.stepMappingService.getIssueChannel();
            UUID flowId = routingInBean.getFlowId()
                .orElse(this.stepMappingService.getUnknownFlowId());
            UUID flowKeyId = routingInBean.getFlowKeyId();
            UUID originStepId = routingInBean.getOriginStep();
            sendIssueMessage(this.flowExceptionsRepository, e, flowId, flowKeyId, originStepId, issueChannel, Boolean.TRUE);
        }
    }

    private static void terminateProcessAsCompleteSuccess(ProcessStep previousProcessStep, ProcessStepRepository processStepRepository) {
        previousProcessStep.setStepStatus(StepStatus.COMPLETED);
        previousProcessStep.setUpdatedAt(ZonedDateTime.now());
        previousProcessStep.setEndTime(ZonedDateTime.now());
        processStepRepository.saveAndFlush(previousProcessStep);
    }

    private static FlowStepInbean buildNextMessageAndSaveProcess(
        UUID nextStepId,
        UUID flowId,
        UUID flowKeyId,
        String flowDataId,
        ProcessStepRepository processStepRepository
    ) {
        ProcessStep nextProcessStep = ProcessStep.builder()
            .stepId(nextStepId)
            .flowId(flowId)
            .flowKeyId(flowKeyId)
            .dataId(flowDataId)
            .startTime(ZonedDateTime.now())
            .createdAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();
        nextProcessStep = processStepRepository.saveAndFlush(nextProcessStep);
        return FlowStepInbean.builder()
            .flowId(Optional.of(flowId))
            .flowKeyId(flowKeyId)
            .flowDataId(flowDataId)
            .stepKeyId(nextProcessStep.getId())
            .build();
    }

    private static void sendIssueMessage(
        FlowExceptionsRepository flowExceptionsRepository,
        Exception sendingMessageException,
        UUID flowId,
        UUID flowKeyId,
        UUID originStepId,
        MessageChannel issueChannel,
        Boolean flowEndingException
    ) {
        UUID exceptionId = ExceptionUtils.storeExceptionInFlowToTheDatabase(flowExceptionsRepository, sendingMessageException);
        IssueHandlerInbean issueHandlerInbean = IssueHandlerInbean.builder()
            .flowId(Optional.of(flowId))
            .flowKeyId(flowKeyId)
            .originStepId(originStepId)
            .exceptionIdOnOriginStep(exceptionId)
            .flowEndingException(flowEndingException)
            .build();
        Message<IssueHandlerInbean> issueMessage = MessageBuilder.withPayload(issueHandlerInbean).build();
        // TODO :: no Check on the sending for issue queue, otherwise The service is on fatal state
        log.info("Sending Message to issue channel '{}'", issueHandlerInbean);
        issueChannel.send(issueMessage);
    }

    private static void sendIssueMessage(
        UUID exceptionId,
        UUID flowId,
        UUID flowKeyId,
        UUID originStepId,
        MessageChannel issueChannel,
        Boolean flowEndingException
    ) {
        IssueHandlerInbean issueHandlerInbean = IssueHandlerInbean.builder()
            .flowId(Optional.of(flowId))
            .flowKeyId(flowKeyId)
            .originStepId(originStepId)
            .exceptionIdOnOriginStep(exceptionId)
            .flowEndingException(flowEndingException)
            .build();
        Message<IssueHandlerInbean> issueMessage = MessageBuilder.withPayload(issueHandlerInbean).build();
        // TODO :: no Check on the sending for issue queue, otherwise The service is on fatal state
        issueChannel.send(issueMessage);
    }
}
