package com.projeto.lms.repository;

import com.projeto.lms.model.Certificado;
import com.projeto.lms.model.MatriculaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// O tipo da PK também é a nossa classe MatriculaId (pois a FK é a PK)
public interface CertificadoRepository extends JpaRepository<Certificado, MatriculaId> {
}