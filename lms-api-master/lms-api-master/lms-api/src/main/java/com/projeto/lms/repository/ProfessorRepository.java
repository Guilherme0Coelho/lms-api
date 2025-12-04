package com.projeto.lms.repository;

import com.projeto.lms.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    // CRUD básico (save, findById, findAll, delete) já está disponível.

    // Método para o Spring Security carregar o Professor pelo Email
    Optional<Professor> findByEmail(String email);
}