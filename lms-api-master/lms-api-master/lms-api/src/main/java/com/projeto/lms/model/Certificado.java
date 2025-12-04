package com.projeto.lms.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Certificado {

    @Id
    private MatriculaId id;

    private LocalDate dataEmissao = LocalDate.now();
    private String hashValidacao;

    @OneToOne
    @MapsId
    @JoinColumns({
            @JoinColumn(name = "aluno_id"),
            @JoinColumn(name = "curso_id")
    })
    private Matricula matricula;

    public Certificado(Matricula matricula, String hashValidacao) {
        this.matricula = matricula;
        this.id = matricula.getId();
        this.hashValidacao = hashValidacao;
    }

   
    public MatriculaId getId() {
        return id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    
    public void setId(MatriculaId id) { this.id = id; }
    public void setMatricula(Matricula matricula) { this.matricula = matricula; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public void setHashValidacao(String hashValidacao) { this.hashValidacao = hashValidacao; }
}
