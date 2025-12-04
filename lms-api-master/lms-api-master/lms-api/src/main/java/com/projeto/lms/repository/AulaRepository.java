package com.projeto.lms.repository;

import com.projeto.lms.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
   
    @Query("SELECT SUM(a.duracaoMinutos) FROM Aula a WHERE a.modulo.id = :moduloId")
    Integer sumDuracaoByModuloId(@Param("moduloId") Long moduloId);
}
