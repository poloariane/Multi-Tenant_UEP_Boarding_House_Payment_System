package com.uepbh.controller;

import com.uepbh.dto.LoginRequest;
import com.uepbh.dto.LoginResponse;
import com.uepbh.dto.OwnerRegistrationRequest;
import com.uepbh.entity.Owner;
import com.uepbh.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login/owner")
    public ResponseEntity<LoginResponse> loginOwner(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginOwner(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/tenant")
    public ResponseEntity<LoginResponse> loginTenant(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginTenant(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/owner")
    public ResponseEntity<Owner> registerOwner(@RequestBody OwnerRegistrationRequest request) {
        Owner owner = authService.registerOwner(request);
        return ResponseEntity.ok(owner);
    }
}
