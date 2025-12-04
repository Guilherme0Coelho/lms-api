package com.projeto.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.lms.model.Professor;
import com.projeto.lms.service.ProfessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Carrega apenas o contexto web do Professor e desliga a segurança (login)
@WebMvcTest(controllers = ProfessorController.class, properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc; //  navegador/Postman

    @MockBean
    private ProfessorService professorService; // Simula o Service

    @Autowired
    private ObjectMapper objectMapper; // 

    @Test
    void deveCriarProfessorComSucesso() throws Exception {
        
        Professor professorEnviado = new Professor();
        professorEnviado.setNome("Mestre Yoda");
        professorEnviado.setEmail("yoda@jedi.com");

        Professor professorSalvo = new Professor();
        professorSalvo.setId(1L);
        professorSalvo.setNome("Mestre Yoda");
        professorSalvo.setEmail("yoda@jedi.com");

        
        when(professorService.save(any(Professor.class))).thenReturn(professorSalvo);

        
        mockMvc.perform(post("/api/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professorEnviado)))
                .andExpect(status().isOk()) // Seu controller retorna 200 OK no create
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Mestre Yoda"));
    }

    @Test
    void deveListarTodosProfessores() throws Exception {
        
        Professor p1 = new Professor();
        p1.setId(1L);
        p1.setNome("Professor Xavier");

        Professor p2 = new Professor();
        p2.setId(2L);
        p2.setNome("Professor Snape");

        when(professorService.findAll()).thenReturn(Arrays.asList(p1, p2));

       
        mockMvc.perform(get("/api/professores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Professor Xavier"));
    }

    @Test
    void deveBuscarProfessorPorIdComSucesso() throws Exception {
        
        Long id = 1L;
        Professor p1 = new Professor();
        p1.setId(id);
        p1.setNome("Professor Girafales");

        when(professorService.findById(id)).thenReturn(Optional.of(p1));

        
        mockMvc.perform(get("/api/professores/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Professor Girafales"));
    }

    @Test
    void deveRetornar404QuandoProfessorNaoExiste() throws Exception {
        
        Long id = 99L;
        when(professorService.findById(id)).thenReturn(Optional.empty());

        
        mockMvc.perform(get("/api/professores/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarProfessor() throws Exception {
        
        Long id = 1L;
        doNothing().when(professorService).delete(id);

        
        mockMvc.perform(delete("/api/professores/{id}", id))
                .andExpect(status().isNoContent()); // Espera 204
    }
}
