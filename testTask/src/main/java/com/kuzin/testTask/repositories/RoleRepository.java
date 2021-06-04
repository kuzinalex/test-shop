package com.kuzin.testTask.repositories;

import com.kuzin.testTask.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
