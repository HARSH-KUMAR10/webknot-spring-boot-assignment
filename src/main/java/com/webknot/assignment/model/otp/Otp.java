package com.webknot.assignment.model.otp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue
    private Long otpId;

    private String email;

    private Long otp;

    @CreationTimestamp
    private LocalDateTime creationTime;

    public Otp(String email, Long otp){
        this.email = email;
        this.otp = otp;
    }
}
