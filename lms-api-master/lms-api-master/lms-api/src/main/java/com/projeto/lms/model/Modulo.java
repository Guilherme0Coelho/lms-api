package com.projeto.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modulo {
    public Long getId() {
        return id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Integer ordem;

    // Relacionamento Muitos-para-Um (N:1) com Curso
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}