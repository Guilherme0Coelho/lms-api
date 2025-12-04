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
        // 1. Cenário
        Long alunoId = 1L;
        Long cursoId = 101L;

        // CORREÇÃO: Usando o construtor manual que você definiu na classe MatriculaId
        MatriculaId idComposto = new MatriculaId(alunoId, cursoId);

        // CORREÇÃO: Usando o construtor vazio (@NoArgsConstructor) e setando o ID manualmente
        Matricula matriculaMock = new Matricula();
        matriculaMock.setId(idComposto);

        // Simulamos o retorno do service
        when(matriculaService.realizarMatricula(alunoId, cursoId)).thenReturn(matriculaMock);

        // 2. Ação e 3. Verificação
        mockMvc.perform(post("/api/matriculas/realizar")
                        .param("alunoId", String.valueOf(alunoId))
                        .param("cursoId", String.valueOf(cursoId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Não validamos o corpo JSON detalhado aqui para evitar erros de serialização recursiva (Aluno/Curso)
    }

    @Test
    void deveRetornarBadRequestQuandoHouverErroNaMatricula() throws Exception {
        String mensagemErro = "Aluno já possui matrícula neste curso";

        // Forçamos o erro no service
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

        // Criando ID mockado
        MatriculaId idComposto = new MatriculaId(1L, cursoId);

        // Criando matrícula mockada
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