package com.projeto.lms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf; // Usado como username

    @Column(nullable = false)
    private String senha; // <-- NOVO CAMPO: Senha Criptografada

    @Column(nullable = false)
    private String roles; // <-- NOVO CAMPO: Ex: "ROLE_ALUNO"

    // Getters e Setters (Mantidos/Adicionados)
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() { // <-- NOVO GETTER
        return senha;
    }

    public void setSenha(String senha) { // <-- NOVO SETTER
        this.senha = senha;
    }

    public String getRoles() { // <-- NOVO GETTER
        return roles;
    }

    public void setRoles(String roles) { // <-- NOVO SETTER
        this.roles = roles;
    }
}