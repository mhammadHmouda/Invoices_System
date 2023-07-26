package com.harri.training1.repositories;

import com.harri.training1.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r.id from Role r where r.name like :name")
    Long findIdByRoleName(@Param("name") String roleName);
}
