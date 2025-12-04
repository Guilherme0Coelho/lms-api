package com.projeto.lms.config;

import com.projeto.lms.model.Aluno;
import com.projeto.lms.model.Professor;
import com.projeto.lms.repository.AlunoRepository;
import com.projeto.lms.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe responsável por inicializar dados na base de dados H2 ao iniciar a aplicação.
 * Isso garante que haja usuários (Aluno e Professor) para testar a autenticação.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Senha que será usada: "senha123"

            // 1. Criar um Professor (ADMIN)
            Professor admin = new Professor();
            admin.setNome("Administrador Principal");
            admin.setEmail("admin@lms.com"); // Email é o username para o Professor
            admin.setSenha(passwordEncoder.encode("senha123")); // Criptografando a senha
            admin.setRoles("ROLE_ADMIN"); // A role deve ter o prefixo 'ROLE_'

            // Garantir que a senha foi salva antes de inserir
            professorRepository.save(admin);
            System.out.println("✅ Professor/Admin Criado: admin@lms.com / senha123");

            // 2. Criar um Professor (Comum)
            Professor prof = new Professor();
            prof.setNome("Prof. Teste");
            prof.setEmail("professor@lms.com");
            prof.setSenha(passwordEncoder.encode("senha123"));
            prof.setRoles("ROLE_PROFESSOR");
            professorRepository.save(prof);
            System.out.println("✅ Professor Comum Criado: professor@lms.com / senha123");


            // 3. Criar um Aluno
            Aluno aluno = new Aluno();
            aluno.setNome("Aluno Teste");
            aluno.setCpf("12345678901"); // CPF é o username para o Aluno
            aluno.setSenha(passwordEncoder.encode("senha123"));
            aluno.setRoles("ROLE_ALUNO");
            alunoRepository.save(aluno);
            System.out.println("✅ Aluno Criado: 12345678901 / senha123");
        };
    }
}