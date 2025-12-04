package com.projeto.lms.repository;

import com.projeto.lms.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    // Método para o Spring Security carregar o Aluno pelo CPF
    Optional<Aluno> findByCpf(String cpf);
}