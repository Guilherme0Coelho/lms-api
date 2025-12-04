package com.projeto.lms.model;

import jakarta.persistence.*;

import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity

public class Matricula {

    public Matricula() {
    }
    @EmbeddedId
    private MatriculaId id;

    private LocalDate dataMatricula = LocalDate.now();
    private Double progresso = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("alunoId")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cursoId")
    private Curso curso;

    @OneToOne(mappedBy = "matricula", cascade = CascadeType.ALL, orphanRemoval = true)
    private Certificado certificado;

    public Matricula(Aluno aluno, Curso curso) {
        this.aluno = aluno;
        this.curso = curso;
        // Requer get id() nas classes Aluno e Curso para compilar
        this.id = new MatriculaId(aluno.getId(), curso.getId());
    }

    
    public MatriculaId getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Curso getCurso() {
        return curso;
    }

    public Double getProgresso() {
        return progresso;
    }

    public Certificado getCertificado() {
        return certificado;
    }

    
    public void setId(MatriculaId id) { this.id = id; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public void setProgresso(Double progresso) { this.progresso = progresso; }
    public void setCertificado(Certificado certificado) { this.certificado = certificado; }
}
