package com.integraal.ops.integration.flow.beans;

import com.integraal.ops.integration.storage.beans.UserDataType;
import com.integraal.ops.integration.transversal.beans.GenericInbean;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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

    private Optional<Tuple2<UserDataType, UUID>> flowDataId;

    private ZonedDateTime receivedDate;
}
