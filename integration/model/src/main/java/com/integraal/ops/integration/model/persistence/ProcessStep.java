package com.integraal.ops.integration.model.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.time.ZonedDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
@Entity
@Table(name = "process_step")
public class ProcessStep {

    @Id
    private UUID id;

    @Column(name = "step_status")
    @Enumerated(value = EnumType.STRING)
    private StepStatus stepStatus;

    @Column(name = "step_id")
    private UUID stepId;

    @Column(name = "step_key_id")
    private UUID stepKeyId;

    @Column(name = "flow_id")
    private UUID flowId;

    @Column(name = "flow_key_id")
    private UUID flowKeyId;

    @Column(name = "data_id")
    private String dataId;

    @Column(name = "exception_id")
    private UUID exceptionId;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
