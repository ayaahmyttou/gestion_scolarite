package com.gestionscolaire.repository;

import com.gestionscolaire.model.Eleve;
import com.gestionscolaire.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {

    List<Eleve> findByFiliere(Filiere filiere);

    List<Eleve> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    @Query("SELECT e FROM Eleve e LEFT JOIN FETCH e.cours LEFT JOIN FETCH e.dossierAdministratif WHERE e.id = :id")
    Eleve findByIdWithDetails(Long id);

    Long countByFiliere(Filiere filiere);
}
