package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.FlowConfigurationService;
import com.integraal.ops.integration.flow.beans.FlowDefinition;
import com.integraal.ops.integration.flow.beans.StepDefinition;
import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FlowConfigurationServiceImpl implements FlowConfigurationService {

    private final Map<Pair<UUID, UUID>, MessageChannel> flowsToMessageChannels;
    private final Map<Pair<UUID, UUID>, StepDefinition> STEP_DEFINITIONS = new HashMap<>();

    private final GenericApplicationContext applicationContext;

    @Autowired
    public FlowConfigurationServiceImpl(GenericApplicationContext applicationContext) throws ClassNotFoundException {
        flowsToMessageChannels = new HashMap<>();

        initializeFlowAdjacentMatrix(applicationContext, FRANCE_TRAVAIL_FLOW, flowsToMessageChannels);
        this.applicationContext = applicationContext;
    }





    // The value should be cached
    public Map<Pair<UUID, UUID>, MessageChannel> buildFlowsChannel() {
        return flowsToMessageChannels;
    }

    public final static UUID UNKNOWN_FLOW_ID = UUID.fromString("b52243ec-7100-4382-b5e3-6c7308db89db");

    public final static Map<UUID, FlowDefinition> FRANCE_TRAVAIL_FLOW = Map.of(
        UUID.fromString("ce785cd9-fc26-4b99-9134-6275266d08ac"), FlowDefinition.builder()
            .flowId(UUID.fromString("ce785cd9-fc26-4b99-9134-6275266d08ac"))
            .flowName("CANDIDATURE_ANNONCE")
            .entrypointClass("com.integraal.ops.integration.flow.EntrypointServiceExemple1Impl") // TODO :: add class
            .endpointClass("com.integraal.ops.integration.flow.EndpointServiceExemple1Impl") // TODO :: add class
            .rootStep(StepDefinition.builder()
                .stepId(UUID.fromString("f057a8dd-d4e2-4231-8a64-526c75103d99"))
                .stepName("EXTRACT_DATA_CANDIDATURE")
                .handlerClass("com.integraal.ops.integration.flow.LogProcessorService") // TODO :: add class
                .children(List.of(
                    StepDefinition.builder()
                        .stepId(UUID.fromString("89dfbe54-ff70-4ee6-ba3c-4d2996716db0"))
                        .stepName("LOG_INFORMATION_1")
                        .handlerClass("com.integraal.ops.integration.flow.LogProcessorService") // TODO :: add class
                        .children(List.of())
                        .build(),
                    StepDefinition.builder()
                        .stepId(UUID.fromString("2f689a99-9bfc-4628-8841-7756084198ba"))
                        .stepName("INSERT_DATA_CANDIDATURE")
                        .handlerClass("com.integraal.ops.integration.flow.LogProcessorService") // TODO :: add class
                        .children(List.of(
                            StepDefinition.builder()
                                .stepId(UUID.fromString("b051dc7c-e192-4182-8c45-9c8f3fa95c14"))
                                .stepName("LOG_INFORMATION_2")
                                .handlerClass("com.integraal.ops.integration.flow.LogProcessorService") // TODO :: add class
                                .children(List.of())
                                .build(),
                            StepDefinition.builder()
                                .stepId(UUID.fromString("9f548c41-e6b7-46df-b3ad-071bbdae0a33"))
                                .stepName("REDUCE_STATS_CANDIDATURES")
                                .stopOnError(Boolean.FALSE)
                                .stopOnWarning(Boolean.FALSE)
                                .handlerClass("com.integraal.ops.integration.flow.LogProcessorService") // TODO :: add class
                                .children(List.of(
                                    StepDefinition.builder()
                                        .stepId(UUID.fromString("37c13ae1-9736-48bc-b964-8c2bad7f25b2"))
                                        .stepName("ENDPOINT_CANDIDATURE_ANNONCE")
                                        .handlerClass("com.integraal.ops.integration.flow.LogProcessorService") // TODO :: add class
                                        .children(List.of())
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build())
            .build()
    );

    @Override
    public EntrypointService getEntrypointServiceForFlow(UUID flowId) throws ServiceFatalException {
        if (!FRANCE_TRAVAIL_FLOW.containsKey(flowId)) {
            throw new ServiceFatalException("FlowConfigurationServiceImpl - getEntrypointServiceForFlow - unknown flow ID. Here was flowId '" + flowId + "', flows defined '" + FRANCE_TRAVAIL_FLOW.keySet() + "'");
        }
        try {
            String entrypointClassName = FRANCE_TRAVAIL_FLOW.get(flowId).getEntrypointClass();
            Class<?> entrypointClass = Class.forName(entrypointClassName);
            boolean isEntryPointService = EntrypointService.class.isAssignableFrom(entrypointClass);
            if (!isEntryPointService) {
                throw new ServiceFatalException("FlowConfigurationServiceImpl - getEntrypointServiceForFlow - could not get the entrypoint. the class is not an entrypoint. Here was flowId '" + flowId + "', entrypointClass '" + entrypointClass + "'");
            }
            return this.applicationContext.getBean(((Class<EntrypointService>)entrypointClass));
        } catch (Exception e) {
            throw new ServiceFatalException("FlowConfigurationServiceImpl - getEntrypointServiceForFlow - could not get the entrypoint class for flow '" + flowId + "'");
        }

    }



    private static List<Pair<UUID, MessageChannel>> constructFlowDepth(
        GenericApplicationContext applicationContext,
        List<StepDefinition> children
    ) throws ClassNotFoundException {
        List<Pair<UUID, MessageChannel>> flows = new ArrayList<>();
        for (StepDefinition child : children) {
            MessageChannel childChannel = new QueueChannel();
            UUID childStepId = child.getStepId();
            Class<?> handlerClass = Class.forName(child.getHandlerClass());
            boolean isAProcessorService = ProcessorService.class.isAssignableFrom(handlerClass);
            if (!isAProcessorService) {
                throw new ServiceFatalException("FlowConfigurationServiceImpl - constructFlowDepth - Handler Class is not a ProcessorService. Construction of flow is on error. Here was handlerClass '" + handlerClass.getCanonicalName() + "'");
            }
            Object beanHandler = applicationContext.getBean(handlerClass);
            // Create Integration
            IntegrationFlow.from(childChannel)
                .handle(beanHandler, "processFlowMessage").get();
            Pair<UUID, MessageChannel> channelRegister = Pair.of(childStepId, childChannel);
            flows.add(channelRegister);
            List<StepDefinition> childrenChild = child.getChildren();
            var grandChildrenFlowDepth = constructFlowDepth(applicationContext, childrenChild);
            flows.addAll(grandChildrenFlowDepth);
        }
        return flows;
    }

    private static void initializeFlowAdjacentMatrix(
        GenericApplicationContext applicationContext,
        Map<UUID, FlowDefinition> flowsDefinition,
        Map<Pair<UUID, UUID>, MessageChannel> flowsToMessageChannelsRegistry
    ) throws ClassNotFoundException {
        for (var uuidAndFlow : flowsDefinition.entrySet()) {
            FlowDefinition flow = uuidAndFlow.getValue();
            UUID flowId = uuidAndFlow.getKey();
            StepDefinition rootStepDefinition = flow.getRootStep();
            MessageChannel rootMessageChannel = new QueueChannel();
            flowsToMessageChannelsRegistry.put(
                Pair.of(flowId, rootStepDefinition.getStepId()),
                rootMessageChannel
            );
            List<StepDefinition> children = rootStepDefinition.getChildren();
            List<Pair<UUID, MessageChannel>> childrenTreeFlow = constructFlowDepth(applicationContext, children);
            for (Pair<UUID, MessageChannel> child : childrenTreeFlow) {
                UUID childStepId = child.getFirst();
                MessageChannel childChannel = child.getSecond();
                flowsToMessageChannelsRegistry.put(Pair.of(flowId, childStepId), childChannel);
            }
        }
    }

}
