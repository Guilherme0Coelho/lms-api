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

/**
 * Endpoint de Autenticação.
 * Embora usemos HTTP Basic (que é tratado pelo Spring), este controller é útil
 * para testar o login de forma programática ou como base para migração futura para JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    // Injeção do AuthenticationManager via construtor (melhor prática)
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Cria o token de autenticação com as credenciais fornecidas
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Se a autenticação for bem-sucedida, o objeto 'authentication' estará preenchido.
            // O Spring Security lida com a resposta do HTTP Basic, mas podemos retornar
            // detalhes do usuário aqui para confirmar o login.
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 3. Retorna os detalhes básicos do usuário autenticado
            return ResponseEntity.ok(
                    String.format("Autenticação bem-sucedida. Usuário: %s | Roles: %s",
                            userDetails.getUsername(),
                            userDetails.getAuthorities())
            );

        } catch (Exception e) {
            // Captura falhas de autenticação (usuário/senha inválidos)
            return ResponseEntity.status(401).body("Falha na autenticação: Credenciais inválidas.");
        }
    }
}