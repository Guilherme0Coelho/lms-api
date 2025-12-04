package com.projeto.lms.controller;

import com.projeto.lms.model.Matricula;
import com.projeto.lms.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.projeto.lms.model.Matricula;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    // Operação Adicional 2: Processo Composto/Transacional
    // Exemplo de URL: POST /api/matriculas?alunoId=1&cursoId=101
    @PostMapping("/realizar")
    public ResponseEntity<?> realizarMatricula(
            @RequestParam Long alunoId,
            @RequestParam Long cursoId) {
        try {
            Matricula novaMatricula = matriculaService.realizarMatricula(alunoId, cursoId);
            return ResponseEntity.ok(novaMatricula);
        } catch (Exception e) {
            // Retorna 400 Bad Request em caso de erro de lógica de negócio (ex: já matriculado)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Operação Adicional 3: Consulta com Múltiplos Critérios
    // Exemplo de URL: GET /api/matriculas/pendentes?cursoId=101
    @GetMapping("/pendentes")
    public ResponseEntity<List<Matricula>> getMatriculasPendentes(
            @RequestParam Long cursoId) {
        List<Matricula> pendentes = matriculaService.buscarMatriculasPendentes(cursoId);
        return ResponseEntity.ok(pendentes);
    }

    // ... Adicione aqui o CRUD básico para Matricula se necessário.
}