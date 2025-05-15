package com.integraal.ops.integration.flow.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class EntrypointFlowInbean {

    private Map<String, String> inputMetaData;
    // Each implementation of entry point should initialize with the FlowID
    private Object inputData;

    private OffsetDateTime receivedDate;
}
