package com.integraal.ops.integration.utils.containers.beans.configuration;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@With
public final class PostgresConfiguration {
    private String postgresContainerVersion;
    private String username;
    private String password;
    private String dbName;
    private String initScriptPath;
    private String cleanupScriptPath;
}
