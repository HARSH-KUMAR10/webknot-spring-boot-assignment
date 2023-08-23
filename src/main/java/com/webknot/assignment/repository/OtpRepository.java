package com.webknot.assignment.repository;

import com.webknot.assignment.model.otp.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Otp findByEmail(String email);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM Otp e WHERE TIMESTAMPDIFF('MINUTE', e.creationTime, CURRENT_TIMESTAMP) > 5")
//    void deleteExpiredOtps();
}
