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

    // POST: Cria um novo Aluno
    @PostMapping
    // Regra de acesso no SecurityConfig: .requestMatchers(HttpMethod.POST, "/api/alunos").hasRole("ADMIN")
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno) {
        // A senha deve ser codificada no Service antes de salvar!
        Aluno savedAluno = alunoService.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAluno);
    }

    // GET: Retorna todos os Alunos
    @GetMapping
    // Regra de acesso (não explícita, mas se não for liberada, usará o .anyRequest().authenticated())
    public List<Aluno> getAllAlunos() {
        return alunoService.findAll();
    }

    // GET: Retorna um Aluno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getAlunoById(@PathVariable Long id) {
        return alunoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT/PATCH: Atualiza um Aluno (Exemplo de atualização completa, PUT)
    @PutMapping("/{id}")
    // Regra de acesso: .anyRequest().authenticated()
    public ResponseEntity<Aluno> updateAluno(@PathVariable Long id, @RequestBody Aluno alunoDetails) {
        Aluno updatedAluno = alunoService.update(id, alunoDetails);
        return ResponseEntity.ok(updatedAluno);
    }

    // DELETE: Remove um Aluno por ID
    @DeleteMapping("/{id}")
    // Regra de acesso no SecurityConfig: .requestMatchers(HttpMethod.DELETE, "/api/alunos/**").hasRole("ADMIN")
    public ResponseEntity<Void> deleteAluno(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Operação Adicional 4: Resposta Agregada/Combinada
    // Exemplo de URL: GET /api/alunos/1/relatorio
    // Regra de acesso no SecurityConfig: .hasAnyRole("ADMIN", "ALUNO")
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