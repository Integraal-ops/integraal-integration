package com.integraal.ops.integration.flow.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class FlowDefinition {
    private UUID flowId;
    private String flowName;
    private StepDefinition rootStep;
    private String entrypointClass;
    private String endpointClass;
}
