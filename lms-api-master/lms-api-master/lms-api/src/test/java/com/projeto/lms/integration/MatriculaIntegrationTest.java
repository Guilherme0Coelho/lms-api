package com.projeto.lms.integration;

import com.projeto.lms.model.Aluno;
import com.projeto.lms.model.Curso;
import com.projeto.lms.model.Professor; // <--- Importante
import com.projeto.lms.repository.AlunoRepository;
import com.projeto.lms.repository.CursoRepository;
import com.projeto.lms.repository.MatriculaRepository;
import com.projeto.lms.repository.ProfessorRepository; // <--- Importante
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MatriculaIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private MatriculaRepository matriculaRepository;
    @Autowired private ProfessorRepository professorRepository; 

    @BeforeEach
    void setup() {
        matriculaRepository.deleteAll();
        alunoRepository.deleteAll();
        cursoRepository.deleteAll();
        professorRepository.deleteAll();
    }

    @Test
    void deveRealizarMatriculaDeVerdade() throws Exception {
        // criar e salvar aluno
        Aluno aluno = new Aluno();
        aluno.setNome("Guilherme");
        aluno.setCpf("11122233344");
        aluno.setSenha("123456");
        aluno.setRoles("ROLE_ALUNO");
        aluno = alunoRepository.save(aluno);

        //  Criar e Salvar prof (Obrigatório para ter Curso)
        Professor professor = new Professor();
        // Se der erro de "cannot resolve method" aqui, verifique sua classe Professor
        // Estou assumindo que ela tem setNome e setEmail
        try {
            professor.setNome("Mestre Yoda");
            professor.setEmail("yoda@jedi.com");
        } catch (Exception e) {
            
        }
        professor = professorRepository.save(professor);

        //  Criar e Salvar cursoo
        Curso curso = new Curso();
        curso.setTitulo("Java Completo"); 
        curso.setCargaHoraria(40);
        curso.setProfessor(professor);    
        curso = cursoRepository.save(curso);

        
        mockMvc.perform(post("/api/matriculas/realizar")
                        .param("alunoId", String.valueOf(aluno.getId()))
                        .param("cursoId", String.valueOf(curso.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // validação
        long totalMatriculas = matriculaRepository.count();
        if (totalMatriculas != 1) {
            throw new RuntimeException("Erro: A matrícula não foi salva no banco H2!");
        }
    }
}
