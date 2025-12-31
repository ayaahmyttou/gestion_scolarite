package com.gestionscolaire.service;

import com.gestionscolaire.model.Filiere;
import com.gestionscolaire.repository.FiliereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FiliereService {

    @Autowired
    private FiliereRepository filiereRepository;

    public List<Filiere> findAll() {
        return filiereRepository.findAll();
    }

    public Optional<Filiere> findById(Long id) {
        return filiereRepository.findById(id);
    }

    public Filiere findByIdWithDetails(Long id) {
        return filiereRepository.findByIdWithDetails(id);
    }

    public Optional<Filiere> findByCode(String code) {
        return filiereRepository.findByCode(code);
    }

    public Filiere save(Filiere filiere) {
        return filiereRepository.save(filiere);
    }

    public void deleteById(Long id) {
        filiereRepository.deleteById(id);
    }

    public boolean existsByCode(String code) {
        return filiereRepository.existsByCode(code);
    }

    public boolean existsByCodeAndIdNot(String code, Long id) {
        Optional<Filiere> existing = filiereRepository.findByCode(code);
        return existing.isPresent() && !existing.get().getId().equals(id);
    }
}
