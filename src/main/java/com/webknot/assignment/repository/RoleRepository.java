package com.webknot.assignment.repository;

import com.webknot.assignment.model.UserRole;
import com.webknot.assignment.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByName(RoleEnum name);
}
