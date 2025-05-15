package com.integraal.ops.integration.utils.containers.beans;

import com.integraal.ops.integration.utils.containers.beans.configuration.PostgresConfiguration;
import com.integraal.ops.integration.utils.containers.beans.configuration.WiremockConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@With
public final class ContainersConfiguration {
    private Optional<PostgresConfiguration> postgresConfiguration;
    private Optional<WiremockConfiguration> wiremockConfiguration;
}
