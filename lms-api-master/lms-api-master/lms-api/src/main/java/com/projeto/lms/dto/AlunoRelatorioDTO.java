package com.projeto.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO: Objeto para formatar a resposta combinada e agregada.
@Data
@NoArgsConstructor
// @AllArgsConstructor
public class AlunoRelatorioDTO { // <-- CORREÇÃO: Declarar a classe e adicionar "class"

    // 1. Campos (Atributos) devem ser declarados aqui:
    private Long alunoId;
    private String alunoNome;
    private String cursoTitulo;
    private Double progressoMatricula;
    private Boolean certificadoEmitido;
    private String statusGeral;

    // 2. Construtor (manual, para resolver o erro) deve estar aqui:
    public AlunoRelatorioDTO(Long alunoId, String alunoNome, String cursoTitulo, Double progressoMatricula, Boolean certificadoEmitido, String statusGeral) {
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.cursoTitulo = cursoTitulo;
        this.progressoMatricula = progressoMatricula;
        this.certificadoEmitido = certificadoEmitido;
        this.statusGeral = statusGeral;
    }
}