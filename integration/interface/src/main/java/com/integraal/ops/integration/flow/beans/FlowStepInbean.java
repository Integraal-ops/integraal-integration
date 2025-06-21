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
public class FlowStepInbean extends GenericInbean {

    private Optional<UUID> flowId;

    private UUID flowKeyId;

    private  UUID stepKeyId;

    private UUID stepId;

    private String flowDataId;

}
