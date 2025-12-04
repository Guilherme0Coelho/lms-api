package com.projeto.lms.controller;

import com.projeto.lms.model.Matricula;
import com.projeto.lms.model.MatriculaId;
import com.projeto.lms.service.MatriculaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MatriculaController.class, properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
class MatriculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatriculaService matriculaService;

    @Test
    void deveRealizarMatriculaComSucesso() throws Exception {
        
        Long alunoId = 1L;
        Long cursoId = 101L;

        
        MatriculaId idComposto = new MatriculaId(alunoId, cursoId);

       
        Matricula matriculaMock = new Matricula();
        matriculaMock.setId(idComposto);

        
        when(matriculaService.realizarMatricula(alunoId, cursoId)).thenReturn(matriculaMock);

       
        mockMvc.perform(post("/api/matriculas/realizar")
                        .param("alunoId", String.valueOf(alunoId))
                        .param("cursoId", String.valueOf(cursoId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
    }

    @Test
    void deveRetornarBadRequestQuandoHouverErroNaMatricula() throws Exception {
        String mensagemErro = "Aluno já possui matrícula neste curso";

       
        when(matriculaService.realizarMatricula(anyLong(), anyLong()))
                .thenThrow(new RuntimeException(mensagemErro));

        mockMvc.perform(post("/api/matriculas/realizar")
                        .param("alunoId", "1")
                        .param("cursoId", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
    }

    @Test
    void deveBuscarMatriculasPendentes() throws Exception {
        Long cursoId = 101L;

        //  ID mockado
        MatriculaId idComposto = new MatriculaId(1L, cursoId);

        //  matrícula mockada
        Matricula mat1 = new Matricula();
        mat1.setId(idComposto);

        List<Matricula> listaPendentes = Collections.singletonList(mat1);

        when(matriculaService.buscarMatriculasPendentes(cursoId)).thenReturn(listaPendentes);

        mockMvc.perform(get("/api/matriculas/pendentes")
                        .param("cursoId", String.valueOf(cursoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
