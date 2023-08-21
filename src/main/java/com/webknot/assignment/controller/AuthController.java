package com.webknot.assignment.controller;

import java.util.*;
import java.util.stream.Collectors;


import com.webknot.assignment.model.UserRole;
import com.webknot.assignment.model.RoleEnum;
import com.webknot.assignment.model.User;
import com.webknot.assignment.payload.request.ForgetPasswordRequest;
import com.webknot.assignment.payload.request.LoginRequest;
import com.webknot.assignment.payload.response.JwtResponse;
import com.webknot.assignment.payload.response.MessageResponse;
import com.webknot.assignment.repository.RoleRepository;
import com.webknot.assignment.repository.UserRepository;
import com.webknot.assignment.security.jwt.JwtUtils;
import com.webknot.assignment.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webknot.assignment.payload.request.SignupRequest;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<UserRole> userRoles = new HashSet<>();

        if (strRoles == null) {
            UserRole userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            userRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        UserRole adminUserRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userRoles.add(adminUserRole);

                        break;
                    case "mod":
                        UserRole modUserRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userRoles.add(modUserRole);

                        break;
                    default:
                        Optional<UserRole> userRole = roleRepository.findByName(RoleEnum.ROLE_USER);
                        userRoles.add(userRole.get());
                }
            });
        }

        user.setUserRoles(userRoles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        if(forgetPasswordRequest.getEmail()==null || forgetPasswordRequest.getEmail().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No Email found"));
        }
        return ResponseEntity.ok(new MessageResponse("Are you trying to forget password: "+ forgetPasswordRequest.getEmail()));
    }
}
