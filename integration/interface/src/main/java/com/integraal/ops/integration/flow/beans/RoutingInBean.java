package com.integraal.ops.integration.flow.beans;

import com.integraal.ops.integration.transversal.beans.GenericInbean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class RoutingInBean extends GenericInbean {

    private RoutingType routingType;

    private Optional<UUID> flowId;

    private Optional<UUID> flowKeyId;

    private Optional<UUID> originStep;

    private Optional<UUID> originStepKeyId;

    private Optional<UUID> exceptionOnOriginStep;

    private Optional<String> flowDataId;

    private OffsetDateTime receivedDate;
}
