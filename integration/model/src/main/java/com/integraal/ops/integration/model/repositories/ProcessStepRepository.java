package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.persistence.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessStepRepository extends JpaRepository<ProcessStep, UUID> {
}
