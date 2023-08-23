package com.webknot.assignment.repository;

import com.webknot.assignment.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User SET password=?1 WHERE email=?2")
    void updatePasswordByEmail(String password, String email);
}
