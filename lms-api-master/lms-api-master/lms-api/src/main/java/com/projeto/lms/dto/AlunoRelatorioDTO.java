package com.projeto.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
// @AllArgsConstructor
public class AlunoRelatorioDTO { 

    
    private Long alunoId;
    private String alunoNome;
    private String cursoTitulo;
    private Double progressoMatricula;
    private Boolean certificadoEmitido;
    private String statusGeral;

    
    public AlunoRelatorioDTO(Long alunoId, String alunoNome, String cursoTitulo, Double progressoMatricula, Boolean certificadoEmitido, String statusGeral) {
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.cursoTitulo = cursoTitulo;
        this.progressoMatricula = progressoMatricula;
        this.certificadoEmitido = certificadoEmitido;
        this.statusGeral = statusGeral;
    }
}
