package com.integraal.ops.integration.flow.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class StepDefinition {
    private UUID stepId;
    private String stepName;
    private List<StepDefinition> children;
    private String handlerClass;
    // This defines if we force the flow to continue even on Fatal Error (System errors will forcibly stop)
    private Boolean stopOnError = Boolean.TRUE;
    // This defines if non-fatal errors (warnings) should be considered as error
    private Boolean stopOnWarning = Boolean.TRUE;
}
