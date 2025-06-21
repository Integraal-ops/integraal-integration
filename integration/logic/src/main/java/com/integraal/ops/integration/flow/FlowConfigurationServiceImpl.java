package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.FlowDefinition;
import com.integraal.ops.integration.flow.beans.StepDefinition;
import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlowConfigurationServiceImpl implements FlowConfigurationService {

    public final Map<Pair<UUID, UUID>, MessageChannel> flowsToMessageChannels;
    private final Map<Pair<UUID, UUID>, StepDefinition> STEP_DEFINITIONS = new HashMap<>();

    private final GenericApplicationContext applicationContext;
    private final IntegrationFlowContext integrationFlowContext;

    @Autowired
    public FlowConfigurationServiceImpl(
        GenericApplicationContext applicationContext,
        IntegrationFlowContext integrationFlowContext
    ) throws ClassNotFoundException {
        this.integrationFlowContext = integrationFlowContext;
        flowsToMessageChannels = new HashMap<>();

        initializeFlowAdjacentMatrix(applicationContext, integrationFlowContext, FRANCE_TRAVAIL_FLOW, flowsToMessageChannels);
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
            .entrypointClass("com.integraal.ops.integration.flow.StandardEntrypointServiceImpl") // TODO :: add class
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

    @Override
    public Optional<MessageChannel> getMessageChannel(UUID flowId, UUID stepId) {
        return Optional.ofNullable(
            flowsToMessageChannels.get(Pair.of(flowId, stepId))
        );
    }



    // ? ======================== Internals ======================================
    private static MessageChannel buildMessageChannelFromStepDefinition(StepDefinition step) {
        // ? TODO :: 19/06/2025 :: The message channel should depend on some configurations
        // ? The Idea is to define the configuration outside the stepDefinition
        // ? In some sort of Flow Context Registry
        return new QueueChannel();
    }

    private static MessageChannel buildRoutingMessageChannel() {
        return new QueueChannel();
    }

    private static MessageChannel buildIssueMessageChannel() {
        return new QueueChannel();
    }


    private static IntegrationFlow buildIntegrationFlowFromStepDefinition(
        ApplicationContext applicationContext,
        MessageChannel flowChannel,
        StepDefinition stepDefinition
    ) throws ClassNotFoundException {
        Class<?> handlerClass = Class.forName(stepDefinition.getHandlerClass());
        boolean isAProcessorService = ProcessorService.class.isAssignableFrom(handlerClass);
        if (!isAProcessorService) {
            throw new ServiceFatalException("FlowConfigurationServiceImpl - buildIntegrationFlowFromStepDefinition - Handler Class is not a ProcessorService. Construction of flow is on error. Here was handlerClass '" + handlerClass.getCanonicalName() + "'");
        }
        Object beanHandler = applicationContext.getBean(handlerClass);
        return IntegrationFlow.from(flowChannel)
            // ? TODO :: 19/06/2025 :: better document the method name conventions
            .handle(beanHandler, "handleMessageWithRoutingAndIssue").get();
    }


    private static String buildIntegrationFlowId(
        UUID flowId,
        StepDefinition stepDefinition
    ) {
        UUID stepId = stepDefinition.getStepId();
        return String.format("%s-%s", flowId, stepId);
    }

    // * INFO :: This method has a side effect and should be used with caution
    private static void registerIntegrationFlowToSpringContext(
        IntegrationFlowContext integrationFlowContext,
        IntegrationFlow integrationFlow,
        String integrationFlowId
    ) {
        // Do registration of flow
        integrationFlowContext.registration(integrationFlow)
                .id(integrationFlowId)
                .autoStartup(true)
                .register();
    }

    private static List<Pair<UUID, MessageChannel>> constructFlowDepth(
        GenericApplicationContext applicationContext,
        IntegrationFlowContext integrationFlowContext,
        UUID flowId,
        List<StepDefinition> children
    ) throws ClassNotFoundException {
        List<Pair<UUID, MessageChannel>> flows = new ArrayList<>();
        for (StepDefinition child : children) {
            // * Build all necessary beans/object to represent the Step in Spring definition
            MessageChannel childChannel = buildMessageChannelFromStepDefinition(child);
            IntegrationFlow integrationFlow = buildIntegrationFlowFromStepDefinition(applicationContext, childChannel, child);
            String integrationFlowId = buildIntegrationFlowId(flowId, child);

            // * Register the Step/Flow to the Spring Context
            registerIntegrationFlowToSpringContext(integrationFlowContext, integrationFlow, integrationFlowId);

            // * Register the Step/Flow for the internal mechanism
            // * Mainly for Routing and dynamic dispatch
            UUID childStepId = child.getStepId();
            Pair<UUID, MessageChannel> channelRegister = Pair.of(childStepId, childChannel);

            flows.add(channelRegister);

            // * recursive call to perform the BFS Algo
            List<StepDefinition> childrenChild = child.getChildren();
            var grandChildrenFlowDepth = constructFlowDepth(applicationContext, integrationFlowContext, flowId, childrenChild);
            flows.addAll(grandChildrenFlowDepth);
        }
        return flows;
    }

    private static void initializeFlowAdjacentMatrix(
        GenericApplicationContext applicationContext,
        IntegrationFlowContext integrationFlowContext,
        Map<UUID, FlowDefinition> flowsDefinition,
        Map<Pair<UUID, UUID>, MessageChannel> flowsToMessageChannelsRegistry
    ) throws ClassNotFoundException {
        // * INFO :: BUILD Routing Flow

        MessageChannel routingChannel = buildRoutingMessageChannel();
        // ? Register the message channel as Spring bean to be available for Autowired
        applicationContext.registerBean("routingChannel", MessageChannel.class, () -> routingChannel);
        Object routingBeanHandler = applicationContext.getBean(RoutingService.class);
        IntegrationFlow routingIntegrationFlow = IntegrationFlow.from(routingChannel)
            .handle(routingBeanHandler, "handleMessage").get();
        String routingIntegrationFlowId = "routingFlow";

        registerIntegrationFlowToSpringContext(integrationFlowContext, routingIntegrationFlow, routingIntegrationFlowId);

        // * INFO :: BUILD Issue Flow
        MessageChannel issueChannel = buildIssueMessageChannel();
        // ? Register the message channel as Spring bean to be available for Autowired
        applicationContext.registerBean("issueChannel", MessageChannel.class, () -> issueChannel);
        Object issueBeanHandler = applicationContext.getBean(IssueHandlerService.class);
        IntegrationFlow issueIntegrationFlow = IntegrationFlow.from(issueChannel)
            .handle(issueBeanHandler, "handleMessage").get();
        String issueIntegrationFlowId = "issueFlow";
        registerIntegrationFlowToSpringContext(integrationFlowContext, issueIntegrationFlow, issueIntegrationFlowId);


        // Build Each Flow :: DAG steps
        for (var uuidAndFlow : flowsDefinition.entrySet()) {
            FlowDefinition flow = uuidAndFlow.getValue();
            UUID flowId = uuidAndFlow.getKey();
            StepDefinition rootStepDefinition = flow.getRootStep();
            // * Register the Root step of the flow
            MessageChannel rootMessageChannel = buildMessageChannelFromStepDefinition(rootStepDefinition);
            IntegrationFlow integrationFlow = buildIntegrationFlowFromStepDefinition(applicationContext, rootMessageChannel, rootStepDefinition);
            String integrationFlowId = buildIntegrationFlowId(flowId, rootStepDefinition);
            registerIntegrationFlowToSpringContext(integrationFlowContext, integrationFlow, integrationFlowId);

            // ? Register the Flow entrypoint
            flowsToMessageChannelsRegistry.put(
                Pair.of(flowId, rootStepDefinition.getStepId()),
                rootMessageChannel
            );
            List<StepDefinition> children = rootStepDefinition.getChildren();
            List<Pair<UUID, MessageChannel>> childrenTreeFlow = constructFlowDepth(applicationContext, integrationFlowContext, flowId, children);
            for (Pair<UUID, MessageChannel> child : childrenTreeFlow) {
                UUID childStepId = child.getFirst();
                MessageChannel childChannel = child.getSecond();
                flowsToMessageChannelsRegistry.put(Pair.of(flowId, childStepId), childChannel);
            }
        }
    }
}
