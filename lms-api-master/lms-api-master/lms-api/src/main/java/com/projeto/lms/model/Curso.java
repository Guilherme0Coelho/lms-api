package com.projeto.lms.model;

import jakarta.persistence.*;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Integer cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Modulo> modulos = new ArrayList<>();

    
    public Long getId() {
        return id;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public String getTitulo() {
        return titulo;
    }

    
    public void setId(Long id) { this.id = id; }
    public void setModulos(List<Modulo> modulos) { this.modulos = modulos; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setCargaHoraria(Integer cargaHoraria) { this.cargaHoraria = cargaHoraria; }
}
