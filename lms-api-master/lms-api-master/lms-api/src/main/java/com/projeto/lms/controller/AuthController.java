package com.projeto.lms.controller;

import com.projeto.lms.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            
            return ResponseEntity.ok(
                    String.format("Autenticação bem-sucedida. Usuário: %s | Roles: %s",
                            userDetails.getUsername(),
                            userDetails.getAuthorities())
            );

        } catch (Exception e) {
            
            return ResponseEntity.status(401).body("Falha na autenticação: Credenciais inválidas.");
        }
    }
}
