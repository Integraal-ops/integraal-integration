package com.integraal.ops.integration.utils.applications.components;

import com.integraal.ops.integration.flow.FlowConfigurationService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class ApplicationComponents {
    private FlowConfigurationService flowConfigurationService;
}
