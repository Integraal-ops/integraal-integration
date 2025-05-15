package com.integraal.ops.integration.utils.containers;

public class ContainerConstants {

    public static final String POSTGRES_DOCKER_IMAGE_TAG = "postgres:16.4";

    public static final String POSTGRES_USERNAME = "postgres";
    public static final String POSTGRES_PASSWORD = "postgres";
    public static final String POSTGRES_DATABASE_NAME = "integraal";

    public final static String WIREMOCK_DOCKER_IMAGE_TAG = "wiremock/wiremock:3.9.1";
    public final static String WIREMOCK_FRANCE_TRAVAIL_SCENARIO_DIRECTORY = "wiremock/francetravail/";
    public final static String WIREMOCK_CONTAINER_PATH = "/home/wiremock/mappings/";
}
