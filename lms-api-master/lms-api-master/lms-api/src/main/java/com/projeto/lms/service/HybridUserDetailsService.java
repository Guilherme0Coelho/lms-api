package com.projeto.lms.service;

import com.projeto.lms.model.Aluno;
import com.projeto.lms.model.Professor;
import com.projeto.lms.repository.AlunoRepository;
import com.projeto.lms.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HybridUserDetailsService implements UserDetailsService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Tenta encontrar como Aluno (Usando CPF)
        Optional<Aluno> alunoOpt = alunoRepository.findByCpf(username);
        if (alunoOpt.isPresent()) {
            Aluno aluno = alunoOpt.get();
            Collection<? extends GrantedAuthority> authorities = parseRoles(aluno.getRoles());

            // Cria um UserDetails: (username, password, authorities)
            return new User(aluno.getCpf(), aluno.getSenha(), authorities);
        }

        // 2. Tenta encontrar como Professor (Usando Email)
        Optional<Professor> profOpt = professorRepository.findByEmail(username);
        if (profOpt.isPresent()) {
            Professor prof = profOpt.get();
            Collection<? extends GrantedAuthority> authorities = parseRoles(prof.getRoles());

            // Cria um UserDetails: (username, password, authorities)
            return new User(prof.getEmail(), prof.getSenha(), authorities);
        }

        // 3. Usuário não encontrado
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }

    private Collection<? extends GrantedAuthority> parseRoles(String roles) {
        if (roles == null || roles.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}