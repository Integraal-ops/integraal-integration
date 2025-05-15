package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.FlowStepInbean;
import com.integraal.ops.integration.flow.beans.RoutingInBean;
import com.integraal.ops.integration.transversal.services.LogicService;

import java.util.Optional;
import java.util.UUID;

public interface ProcessorService extends LogicService {

    public Object processFlowMessage(Object data) throws Exception;

    public String registerNewData(Object newData, Object oldData, String oldDataId) throws Exception;

    public UUID getStepUUID();

    public Object getDataFromId(String dataId) throws Exception;

    default void handleMessageWithRoutingAndIssue(FlowStepInbean flowStepInbean) {
        UUID stepUUID = getStepUUID();
        Optional<UUID> flowId = flowStepInbean.getFlowId();
        UUID flowKeyId = flowStepInbean.getFlowKeyId();
        UUID stepKeyId = UUID.randomUUID();
        String dataUUID = flowStepInbean.getFlowDataId();
        try {
            Object dataInput = getDataFromId(dataUUID);
            Object outputData = processFlowMessage(dataInput);
            String newDataId = registerNewData(outputData, dataInput, dataUUID);
            RoutingInBean routingInBean = RoutingInBean.builder()
                    .flowKeyId(flowKeyId)
                    .flowId(flowId)
                    .originStep(stepUUID)
                    .originStepKeyId(stepKeyId)
                    .flowDataId(dataUUID)
                    .exceptionOnOriginStep(Optional.empty())
                    .build();
            // Send data to the routing channel
        } catch (Exception e) {
            // Convert Exception with exceptionUtils
            RoutingInBean routingInBean = RoutingInBean.builder()
                    .flowKeyId(flowKeyId)
                    .flowId(flowId)
                    .originStep(stepUUID)
                    .originStepKeyId(stepKeyId)
                    .flowDataId(dataUUID)
                    .exceptionOnOriginStep(null)
                    .build();
            // Send data to the routing channel
        }
    }
}
