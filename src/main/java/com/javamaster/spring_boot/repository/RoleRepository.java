package com.javamaster.spring_boot.repository;

import com.javamaster.spring_boot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
