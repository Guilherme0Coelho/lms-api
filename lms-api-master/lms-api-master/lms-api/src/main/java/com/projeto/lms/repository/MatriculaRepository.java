package com.projeto.lms.repository;

import com.projeto.lms.model.Matricula;
import com.projeto.lms.model.MatriculaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
// O tipo da PK é a nossa classe MatriculaId
public interface MatriculaRepository extends JpaRepository<Matricula, MatriculaId> {
    @Query("SELECT m FROM Matricula m LEFT JOIN Certificado c ON m.id = c.id WHERE m.curso.id = :cursoId AND c.id IS NULL")
    List<Matricula> findMatriculasSemCertificadoByCurso(@Param("cursoId") Long cursoId);
}