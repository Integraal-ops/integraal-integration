package com.integraal.ops.integration.flow.beans;

import com.integraal.ops.integration.transversal.beans.GenericInbean;
import com.integraal.ops.integration.transversal.contexts.FlowMethodContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class ProcessorInbean extends GenericInbean {
    private Map<String, String> inputMetaData;
    private Object inputData;
    private FlowMethodContext flowMethodContext;
}
