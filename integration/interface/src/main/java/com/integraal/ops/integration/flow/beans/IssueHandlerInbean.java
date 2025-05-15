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
public class IssueHandlerInbean extends GenericInbean {

    private Optional<UUID> flowId;

    private UUID flowKeyId;

    private UUID exceptionIdOnOriginStep;

    private UUID originStepId;

    private UUID originStepKeyId;

    // By default all errors are fatal
    private Boolean flowEndingException = Boolean.TRUE;
}
