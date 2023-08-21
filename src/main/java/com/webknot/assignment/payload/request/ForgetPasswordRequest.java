package com.webknot.assignment.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPasswordRequest {
    private String email;
}
