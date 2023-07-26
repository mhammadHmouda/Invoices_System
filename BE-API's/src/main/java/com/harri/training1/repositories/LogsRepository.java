package com.harri.training1.repositories;

import com.harri.training1.models.entities.Log;
import com.harri.training1.models.enums.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepository extends JpaRepository<Log, Long> {

    @Query("select l from Log l where l.action = :action")
    List<Log> findByActionType(@Param("action") Action action);
}
