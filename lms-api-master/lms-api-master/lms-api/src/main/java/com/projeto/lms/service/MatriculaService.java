package com.projeto.lms.service;

import com.projeto.lms.model.Aluno;
import com.projeto.lms.model.Curso;
import com.projeto.lms.model.Matricula;
import com.projeto.lms.repository.AlunoRepository;
import com.projeto.lms.repository.CursoRepository;
import com.projeto.lms.repository.MatriculaRepository;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    /**
     * Processo composto/transacional: Registra a matrícula.
     */
    @Transactional
    public Matricula realizarMatricula(Long alunoId, Long cursoId) {
        // Validações
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado."));

        // Validação de Duplicidade (Requer get id() corrigido em Matricula)
        if (matriculaRepository.findById(new Matricula(aluno, curso).getId()).isPresent()) {
            throw new IllegalStateException("Aluno já matriculado neste curso.");
        }

        Matricula novaMatricula = new Matricula(aluno, curso);
        return matriculaRepository.save(novaMatricula);
    }

    /**
     * Consulta com Múltiplos Critérios: Busca matrículas pendentes de Certificado.
     */
    public List<Matricula> buscarMatriculasPendentes(Long cursoId) {
        // Requer o método findMatriculasSemCertificadoByCurso no Repositório
        return matriculaRepository.findMatriculasSemCertificadoByCurso(cursoId);
    }
}