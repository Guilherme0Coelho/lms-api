package com.projeto.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.lms.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

// addFilters = false desliga a segurança geral pra focar só no teste do método
@WebMvcTest(controllers = AuthController.class, properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager; // Mockamos o cara que valida a senha

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveAutenticarUsuarioComSucesso() throws Exception {
        // 1. Cenario (Arrange)
        // Crie o objeto de login (ajuste conforme seu DTO real, se for construtor ou setters)
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("123456");

        // Mockamos o objeto Authentication e UserDetails que o Spring Security retorna
        Authentication authMock = mock(Authentication.class);
        UserDetails userDetailsMock = mock(UserDetails.class);

        // Ensinamos os mocks a responderem
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("admin");
        when(userDetailsMock.getAuthorities()).thenReturn(null); // Pode retornar lista vazia ou null pro teste simples

        // 2. Ação e 3. Verificação
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) // Espera 200
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Autenticação bem-sucedida")));
    }

    @Test
    void deveRetornar401QuandoCredenciaisInvalidas() throws Exception {
        // 1. Cenario
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("errado");
        loginRequest.setPassword("errado");

        // Simulamos que o Manager vai lançar erro de credencial inválida
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // 2. Ação e 3. Verificação
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized()) // Espera 401
                .andExpect(content().string("Falha na autenticação: Credenciais inválidas."));
    }
}