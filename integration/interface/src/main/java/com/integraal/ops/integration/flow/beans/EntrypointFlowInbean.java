package com.integraal.ops.integration.flow.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class EntrypointFlowInbean {
    // * The flow Key ID is optional as there could be a discovery process
    private Optional<UUID> flowKeyId;
    private Map<String, String> inputMetaData;
    private Object inputData;
    private ZonedDateTime receivedDate;
}
