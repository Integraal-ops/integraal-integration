package com.integraal.ops.integration.model.alterations;

import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.ProcessStep;

import java.util.UUID;

public sealed interface ProcessStepAlteration permits
    ProcessStepAlteration.ProcessStepAlterationCreate,
    ProcessStepAlteration.ProcessStepAlterationDelete,
    ProcessStepAlteration.ProcessStepAlterationUpdate
{
    enum ProcessAlterationField {
        STEP_STATUS
    }
    record ProcessStepAlterationUpdate(
        ProcessAlterationField alterationField
    ) implements ProcessStepAlteration {}


    record ProcessStepAlterationCreate(
        ProcessStep processStepDto
    ) implements ProcessStepAlteration {}

    record ProcessStepAlterationDelete(
        UUID id
    ) implements ProcessStepAlteration {}
}
