package com.gestionscolaire.service;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.model.Filiere;
import com.gestionscolaire.repository.CoursRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Transactional
@Service
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    public List<Cours> findAll() {
        return coursRepository.findAll();
    }

    public Optional<Cours> findById(Long id) {
        return coursRepository.findById(id);
    }

    public Cours findByIdWithEleves(Long id) {
        return coursRepository.findByIdWithEleves(id);
    }

    public List<Cours> findByFiliere(Filiere filiere) {
        return coursRepository.findByFiliere(filiere);
    }

    public Optional<Cours> findByCode(String code) {
        return coursRepository.findByCode(code);
    }

    public Cours save(Cours cours) {
        return coursRepository.save(cours);
    }

    public void deleteById(Long id) {
        coursRepository.deleteById(id);
    }

    public boolean existsByCode(String code) {
        return coursRepository.existsByCode(code);
    }

    public boolean existsByCodeAndIdNot(String code, Long id) {
        Optional<Cours> existing = coursRepository.findByCode(code);
        return existing.isPresent() && !existing.get().getId().equals(id);
    }

}
