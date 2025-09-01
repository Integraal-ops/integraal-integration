package com.integraal.ops.integration.data.predicates;

import org.jooq.Condition;

import java.util.UUID;

import static com.integraal.ops.integration.model.persistence.jooq.generated.tables.ProcessStep.PROCESS_STEP;

public class ProcessStepPredicate {


    public static Condition processWithFlowId(UUID flowId) {
        return PROCESS_STEP.FLOW_ID.eq(flowId);
    }

    public static Condition processWithFlowKeyId(UUID flowKeyId) {
        return PROCESS_STEP.FLOW_KEY_ID.eq(flowKeyId);
    }

    public static Condition processWithStepId(UUID stepId) {
        return PROCESS_STEP.STEP_ID.eq(stepId);
    }

    public static Condition processWithStepKeyId(UUID stepKeyId) {
        return PROCESS_STEP.STEP_KEY_ID.eq(stepKeyId);
    }
}
