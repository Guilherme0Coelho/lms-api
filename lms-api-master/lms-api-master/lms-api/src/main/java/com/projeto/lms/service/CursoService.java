package com.projeto.lms.service;

import com.projeto.lms.model.Curso;
import com.projeto.lms.model.Modulo;
import com.projeto.lms.repository.CursoRepository;
import com.projeto.lms.repository.AulaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AulaRepository aulaRepository; // Usado para a agregação

    // ... Métodos CRUD básicos ...

    /**
     * Operação Adicional 1: Cálculo e agregação.
     * Calcula a carga horária total do curso, somando a duração de todas as suas aulas.
     */
    public Integer calcularCargaHorariaTotal(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + cursoId));

        // O erro 'cannot find symbol method getModulos()' está resolvido aqui
        Integer cargaTotal = curso.getModulos().stream()
                .mapToInt(modulo -> {
                    // Chama o método de agregação no repositório
                    Integer duracaoModulo = aulaRepository.sumDuracaoByModuloId(modulo.getId());
                    return duracaoModulo != null ? duracaoModulo : 0;
                })
                .sum();

        return cargaTotal;
    }
}