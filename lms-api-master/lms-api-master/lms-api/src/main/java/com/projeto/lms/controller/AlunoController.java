package com.projeto.lms.controller;

import com.projeto.lms.dto.AlunoRelatorioDTO;
import com.projeto.lms.model.Aluno;
import com.projeto.lms.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    
    @PostMapping
    
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno) {
        // A senha deve ser codificada no Service antes de salvar!
        Aluno savedAluno = alunoService.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAluno);
    }

    
    @GetMapping
   
    public List<Aluno> getAllAlunos() {
        return alunoService.findAll();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getAlunoById(@PathVariable Long id) {
        return alunoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    @PutMapping("/{id}")
    // Regra de acesso: .anyRequest().authenticated()
    public ResponseEntity<Aluno> updateAluno(@PathVariable Long id, @RequestBody Aluno alunoDetails) {
        Aluno updatedAluno = alunoService.update(id, alunoDetails);
        return ResponseEntity.ok(updatedAluno);
    }

   
    @DeleteMapping("/{id}")
    // Regra de acesso no SecurityConfig: .requestMatchers(HttpMethod.DELETE, "/api/alunos/**").hasRole("ADMIN")
    public ResponseEntity<Void> deleteAluno(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/{alunoId}/relatorio")
    public ResponseEntity<List<AlunoRelatorioDTO>> gerarRelatorioGeral(@PathVariable Long alunoId) {
        try {
            List<AlunoRelatorioDTO> relatorio = alunoService.gerarRelatorioGeral(alunoId);
            return ResponseEntity.ok(relatorio);
        } catch (Exception e) {
            // Aqui usamos 404 para EntityNotFoundException do Service
            return ResponseEntity.notFound().build();
        }
    }
}
