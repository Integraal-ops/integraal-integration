package com.integraal.ops.integration.flow.beans;

import com.integraal.ops.integration.transversal.beans.GenericInbean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class EndpointFlowInbean extends GenericInbean {
    // Current Flow ID
    // Defines the Flow with steps
    private Optional<UUID> flowId;

    // KeyFlow defines the id of an iteration of the flow
    private UUID flowKeyId;

    private UUID stepKeyId;

    private String flowDataId;

}
