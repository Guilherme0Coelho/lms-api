package com.projeto.lms.config;

import com.projeto.lms.service.HybridUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private HybridUserDetailsService hybridUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita CSRF 
                .csrf(csrf -> csrf.disable())

                // 2. Permite que o H2 Console seja renderizado em um frame 
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                .authorizeHttpRequests(auth -> auth

                        // 3. Permite o acesso SEM AUTENTICAÇÃO ao H2 Console
                        .requestMatchers("/h2-console/**").permitAll()

                        // Permite o acesso ao endpoint de login POST ***
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // === Regras de Acesso ===

                        // ProfessorController: Apenas ADMIN pode criar/deletar
                        .requestMatchers(HttpMethod.POST, "/api/professores").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/professores/**").hasRole("ADMIN")
                        // Professor e Admin podem ver
                        .requestMatchers(HttpMethod.GET, "/api/professores/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/professores").hasAnyRole("ADMIN", "PROFESSOR")

                        // MatriculaController: Aluno pode se matricular
                        .requestMatchers(HttpMethod.POST, "/api/matriculas/realizar").hasRole("ALUNO")
                        // Professor ou Admin podem ver pendências
                        .requestMatchers(HttpMethod.GET, "/api/matriculas/pendentes").hasAnyRole("ADMIN", "PROFESSOR")

                        // AlunoController: Relatórios de Aluno
                        .requestMatchers(HttpMethod.GET, "/api/alunos/{alunoId}/relatorio").hasAnyRole("ADMIN", "ALUNO")
                        // CRUD de Alunos (Regras de Exemplo)
                        .requestMatchers(HttpMethod.POST, "/api/alunos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/alunos/**").hasRole("ADMIN")

                        // 4. Qualquer outra requisição deve ser autenticada
                        .anyRequest().authenticated()
                )
                .userDetailsService(hybridUserDetailsService) // Define nosso serviço de login híbrido
                .httpBasic(withDefaults()); // Habilita a autenticação HTTP Basic

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Isso usa a configuração interna
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        
        return new BCryptPasswordEncoder();
    }
}
