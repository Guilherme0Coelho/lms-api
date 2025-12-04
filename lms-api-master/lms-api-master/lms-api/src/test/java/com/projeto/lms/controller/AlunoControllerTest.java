package com.projeto.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.lms.dto.AlunoRelatorioDTO;
import com.projeto.lms.model.Aluno;
import com.projeto.lms.service.AlunoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest sobe apenas o contexto web (Controller), não sobe o banco de dados. Muito rápido.
// addFilters = false DESLIGA o Spring Security para o teste (evita erro 403/401 chato agora)
@WebMvcTest(controllers = AlunoController.class, properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula as chamadas HTTP

    @MockBean
    private AlunoService alunoService; // Finge ser o Service (Mock)

    @Autowired
    private ObjectMapper objectMapper; // Transforma Objeto Java em JSON e vice-versa

    @Test
    void deveRetornarTodosAlunos() throws Exception {
        // 1. Cenario (Arrange)
        Aluno aluno1 = new Aluno();
        aluno1.setId(1L);
        aluno1.setNome("João");

        Aluno aluno2 = new Aluno();
        aluno2.setId(2L);
        aluno2.setNome("Maria");

        // Quando o controller chamar o service.findAll(), retorne essa lista
        when(alunoService.findAll()).thenReturn(Arrays.asList(aluno1, aluno2));

        // 2. Ação e 3. Verificação (Act & Assert)
        mockMvc.perform(get("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(jsonPath("$.size()").value(2)) // Espera 2 itens na lista
                .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    void deveRetornarAlunoPorIdComSucesso() throws Exception {
        Long id = 1L;
        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setNome("Teste");

        // Simula que encontrou o aluno
        when(alunoService.findById(id)).thenReturn(Optional.of(aluno));

        mockMvc.perform(get("/api/alunos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Teste"));
    }

    @Test
    void deveRetornar404QuandoAlunoNaoExiste() throws Exception {
        Long id = 99L;
        // Simula que NÃO encontrou (Optional vazio)
        when(alunoService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/alunos/{id}", id))
                .andExpect(status().isNotFound()); // Espera 404
    }

    @Test
    void deveCriarAlunoComSucesso() throws Exception {
        Aluno alunoParaSalvar = new Aluno();
        alunoParaSalvar.setNome("Novo Aluno");

        Aluno alunoSalvo = new Aluno();
        alunoSalvo.setId(10L);
        alunoSalvo.setNome("Novo Aluno");

        // Quando pedir para salvar qualquer coisa, retorne o aluno salvo com ID
        when(alunoService.save(any(Aluno.class))).thenReturn(alunoSalvo);

        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alunoParaSalvar))) // Converte objeto p/ JSON
                .andExpect(status().isCreated()) // Espera 201 Created
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void deveDeletarAluno() throws Exception {
        Long id = 1L;
        // O delete retorna void, então usamos doNothing
        doNothing().when(alunoService).delete(id);

        mockMvc.perform(delete("/api/alunos/{id}", id))
                .andExpect(status().isNoContent()); // Espera 204 No Content
    }
}