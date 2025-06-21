package com.integraal.ops.integration.flow.configurations;

import com.integraal.ops.integration.transversal.beans.GenericConfigurationBean;
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
public class StepBuildConfiguration extends GenericConfigurationBean {
    IntegrationType integrationType;

}
