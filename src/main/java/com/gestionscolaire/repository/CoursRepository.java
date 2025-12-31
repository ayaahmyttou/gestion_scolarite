package com.gestionscolaire.repository;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    List<Cours> findByFiliere(Filiere filiere);

    Optional<Cours> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT c FROM Cours c LEFT JOIN FETCH c.eleves WHERE c.id = :id")
    Cours findByIdWithEleves(Long id);

}
