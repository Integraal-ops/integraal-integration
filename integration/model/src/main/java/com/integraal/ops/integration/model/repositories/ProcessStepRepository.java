package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.persistence.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessStepRepository extends JpaRepository<ProcessStep, UUID> {
}
