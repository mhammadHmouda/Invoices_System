package com.harri.training1.repositories;

import com.harri.training1.models.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends JpaRepository<Log, Long> {
}
