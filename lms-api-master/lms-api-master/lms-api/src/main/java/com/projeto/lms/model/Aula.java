package com.projeto.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Integer duracaoMinutos;

    
    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
}
