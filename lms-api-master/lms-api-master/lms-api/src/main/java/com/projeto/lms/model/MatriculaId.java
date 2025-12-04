package com.projeto.lms.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Embeddable
public class MatriculaId implements Serializable {

    private Long alunoId;
    private Long cursoId;

    
    public MatriculaId() {
    }

   
    public MatriculaId(Long alunoId, Long cursoId) {
        this.alunoId = alunoId;
        this.cursoId = cursoId;
    }

   
    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
}
