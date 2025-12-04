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

    
    @GetMapping("/pendentes")
    public ResponseEntity<List<Matricula>> getMatriculasPendentes(
            @RequestParam Long cursoId) {
        List<Matricula> pendentes = matriculaService.buscarMatriculasPendentes(cursoId);
        return ResponseEntity.ok(pendentes);
    }

    
}
