package com.projeto.lms.controller;

import com.projeto.lms.model.Professor;
import com.projeto.lms.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Define a classe como um controlador REST
@RequestMapping("/api/professores") // Define o caminho base da URL
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    
    @PostMapping
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) {
        Professor savedProfessor = professorService.save(professor);
        return ResponseEntity.ok(savedProfessor);
    }

    
    @GetMapping
    public List<Professor> getAllProfessores() {
        return professorService.findAll();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        return professorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
