package com.integraal.ops.integration.utils.orchestrations.beans.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.boot.WebApplicationType;

import java.util.List;
import java.util.Map;

@Builder
@With
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationToRun {
    private String applicationName;
    private Class<?> applicationClass;
    private List<Class<?>> aopClasses;
    private Map<String, String> applicationProperties;
    private WebApplicationType applicationType;
}
