package com.projeto.lms.service;

import com.projeto.lms.dto.AlunoRelatorioDTO;
import com.projeto.lms.model.Aluno;
import com.projeto.lms.model.Matricula;
import com.projeto.lms.repository.AlunoRepository;
import com.projeto.lms.repository.MatriculaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    // Injetando o PasswordEncoder para criptografar a senha no save
    @Autowired
    private PasswordEncoder passwordEncoder;

    // === MÉTODOS CRUD BÁSICOS ===

    @Transactional
    public Aluno save(Aluno aluno) {
        // CRUCIAL: Codifica a senha antes de salvar um novo usuário/aluno
        String encodedPassword = passwordEncoder.encode(aluno.getSenha());
        aluno.setSenha(encodedPassword);
        return alunoRepository.save(aluno);
    }

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> findById(Long id) {
        return alunoRepository.findById(id);
    }

    @Transactional
    public Aluno update(Long id, Aluno alunoDetails) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado para atualização."));

        // Atualiza campos
        aluno.setNome(alunoDetails.getNome());
        aluno.setRoles(alunoDetails.getRoles());

        // Se uma nova senha for fornecida, codifique e atualize
        if (alunoDetails.getSenha() != null && !alunoDetails.getSenha().isEmpty()) {
            aluno.setSenha(passwordEncoder.encode(alunoDetails.getSenha()));
        }

        return alunoRepository.save(aluno);
    }

    public void delete(Long id) {
        alunoRepository.deleteById(id);
    }

    // === OPERAÇÃO AGREGADA ===

    /**
     * Operação Adicional 4: Resposta agregada ou combinada.
     * Gera um relatório consolidado do desempenho de um aluno em todos os cursos.
     */
    public List<AlunoRelatorioDTO> gerarRelatorioGeral(Long alunoId) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        // Busca todas as matrículas do aluno
        // NOTA: Em um projeto real, seria melhor buscar apenas as matrículas do alunoId
        List<Matricula> todasMatriculas = matriculaRepository.findAll();

        // Filtra e Mapeia a resposta combinada para o DTO
        return todasMatriculas.stream()
                .filter(m -> m.getAluno() != null && m.getAluno().getId().equals(alunoId))
                .map(m -> {
                    boolean certificadoEmitido = m.getCertificado() != null;

                    String status = "Em Andamento";
                    if (certificadoEmitido) {
                        status = "Concluído e Certificado";
                    } else if (m.getProgresso() != null && m.getProgresso() >= 100.0) {
                        status = "Aguardando Emissão";
                    }

                    return new AlunoRelatorioDTO(
                            aluno.getId(),
                            aluno.getNome(),
                            m.getCurso() != null ? m.getCurso().getTitulo() : "Curso Desconhecido",
                            m.getProgresso(),
                            certificadoEmitido,
                            status
                    );
                })
                .collect(Collectors.toList());
    }
}