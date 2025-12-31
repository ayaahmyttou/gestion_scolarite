package com.gestionscolaire.repository;

import com.gestionscolaire.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Long> {

    Optional<Filiere> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT f FROM Filiere f LEFT JOIN FETCH f.eleves LEFT JOIN FETCH f.cours WHERE f.id = :id")
    Filiere findByIdWithDetails(Long id);
}
