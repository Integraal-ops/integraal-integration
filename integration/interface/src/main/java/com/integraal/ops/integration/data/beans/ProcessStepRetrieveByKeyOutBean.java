package com.integraal.ops.integration.data.beans;

import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.ProcessStep;
import com.integraal.ops.integration.transversal.beans.GenericOutbean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class ProcessStepRetrieveByKeyOutBean extends GenericOutbean {
    ProcessStep processStep;
}
