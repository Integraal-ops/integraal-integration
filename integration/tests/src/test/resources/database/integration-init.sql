CREATE TABLE process_step
(
    id           UUID                        NOT NULL,
    step_status  VARCHAR(255),
    step_id      UUID,
    step_key_id  UUID,
    flow_id      UUID,
    flow_key_id  UUID,
    data_id      VARCHAR(255),
    exception_id UUID,
    start_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time     TIMESTAMP WITHOUT TIME ZONE,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_process_step PRIMARY KEY (id)
);


CREATE TABLE flow_exceptions
(
    id             UUID                        NOT NULL,
    exception_type VARCHAR(255),
    message        VARCHAR(255),
    stack_trace    TEXT,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_flow_exceptions PRIMARY KEY (id)
);