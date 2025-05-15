package com.integraal.ops.integration.utils.containers.builders;

import com.integraal.ops.integration.utils.containers.beans.configuration.PostgresConfiguration;
import com.integraal.ops.integration.utils.containers.beans.wrappers.PostgresContainerWrapper;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

@Slf4j
public final class PostgresContainerBuilder {
    public static PostgresContainerWrapper build(PostgresConfiguration configuration) {
        var container = new PostgreSQLContainer(configuration.getPostgresContainerVersion())
            .withDatabaseName(configuration.getDbName())
            .withUsername(configuration.getUsername())
            .withPassword(configuration.getPassword());
        return new PostgresContainerWrapper(
            container,
            configuration.getInitScriptPath(),
            configuration.getCleanupScriptPath()
        );
    }

    public static void startPostgresContainerWithInitialization(PostgresContainerWrapper postgresContainerWrapper) {
        var postgresContainer = postgresContainerWrapper.container();
        String initScript = postgresContainerWrapper.initScript();
        postgresContainer.start();
        // TODO :: 15/05/2025 :: Test QueryString for connection parameters
        var containerDelegate = new JdbcDatabaseDelegate(postgresContainer, "");
        ScriptUtils.runInitScript(containerDelegate, initScript);
    }
}
