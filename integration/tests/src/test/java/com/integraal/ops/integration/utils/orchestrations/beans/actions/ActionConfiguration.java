package com.integraal.ops.integration.utils.orchestrations.beans.actions;

public sealed interface ActionConfiguration permits
        CronActionConfiguration,
        HttpActionConfiguration
{
}
