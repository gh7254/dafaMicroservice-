package com.dafa.authorization.controller;

import com.dafa.authorization.dto.AuthRequest;
import com.dafa.authorization.dto.AuthResponse;
import com.dafa.authorization.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Contoh endpoint yang diproteksi JWT
    @GetMapping("/test-auth")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Halo! Jika kamu melihat pesan ini, berarti Token JWT kamu valid.");
    }
}