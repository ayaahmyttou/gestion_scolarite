package com.gestionscolaire.repository;

import com.gestionscolaire.model.DossierAdministratif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DossierAdministratifRepository extends JpaRepository<DossierAdministratif, Long> {

    Optional<DossierAdministratif> findByNumeroInscription(String numeroInscription);

    boolean existsByNumeroInscription(String numeroInscription);
}
