package com.projeto.lms.service;

import com.projeto.lms.model.Professor;
import com.projeto.lms.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    // CRUD BÁSICO
    public Professor save(Professor professor) {
        return professorRepository.save(professor);
    }

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Optional<Professor> findById(Long id) {
        return professorRepository.findById(id);
    }

    public void delete(Long id) {
        professorRepository.deleteById(id);
    }

    // Futuras operações de negócio específicas para Professor iriam aqui
}