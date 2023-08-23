package com.webknot.assignment.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationAccountOtp {
    private Long otp;
    private String email;
    private String accountPassword;
    private String confirmPassword;
}
