package com.integraal.ops.integration.utils.orchestrations.runners;

import com.integraal.ops.integration.utils.orchestrations.beans.actions.ActionConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.actions.CronActionConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.actions.HttpActionConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public sealed class RunnerActions implements TestStepRunner
        permits RunnerActionsCron, RunnerActionsHttpCall
{
    @Override
    public void run(TestRunConfiguration runConfiguration) throws Exception {
        log.info("==== Running RunnerActions ====");
        for (ActionConfiguration action : runConfiguration.manualActions()) {
            switch (action) {
                case CronActionConfiguration cron ->
                        new RunnerActionsCron().applyRun(cron);
                case HttpActionConfiguration httpActionConfiguration ->
                        new RunnerActionsHttpCall().applyRun(httpActionConfiguration);
            }
        }
    }
}
