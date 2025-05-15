package com.integraal.ops.integration.utils.orchestrations.runners;

import com.integraal.ops.integration.utils.orchestrations.beans.actions.CronActionConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RunnerActionsCron extends RunnerActions {
    public void applyRun(CronActionConfiguration runParameters) throws Exception {
        log.info("Running Cron Test - Not Implemented Yet");
    }
}
