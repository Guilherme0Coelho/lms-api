package com.projeto.lms.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.lms.model.Aluno;
import com.projeto.lms.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Ignora login/segurança
@ActiveProfiles("test") // Usa o banco H2
class AlunoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        alunoRepository.deleteAll();
    }

    @Test
    void deveSalvarAlunoNoBancoDeVerdade() throws Exception {
        // 1. Cenário: Criamos um aluno com CPF e Senha (em vez de email)
        Aluno novoAluno = new Aluno();
        novoAluno.setNome("Teste Integração");
        novoAluno.setCpf("12345678900");     // <--- CORRIGIDO
        novoAluno.setSenha("minhasenha123"); // <--- CORRIGIDO
        novoAluno.setRoles("ROLE_ALUNO");    // <--- CORRIGIDO

        // 2. Ação
        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAluno)))
                .andExpect(status().isCreated()); // Se der erro 404/200 aqui, me avise que ajustamos

        // 3. Validação no Banco
        assert(alunoRepository.count() == 1);
        Aluno salvo = alunoRepository.findAll().get(0);
        assert(salvo.getCpf().equals("12345678900"));
    }

    @Test
    void deveLerDoBancoViaApi() throws Exception {
        // 1. Cenário: Salvando direto no banco
        Aluno aluno = new Aluno();
        aluno.setNome("Aluno Já Salvo");
        aluno.setCpf("98765432100");         // <--- CORRIGIDO
        aluno.setSenha("senhaforte");        // <--- CORRIGIDO
        aluno.setRoles("ROLE_ALUNO");        // <--- CORRIGIDO

        alunoRepository.save(aluno);

        // 2. Ação
        mockMvc.perform(get("/api/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Aluno Já Salvo"));
    }
}