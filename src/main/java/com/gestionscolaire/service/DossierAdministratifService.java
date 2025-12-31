package com.gestionscolaire.service;

import com.gestionscolaire.model.DossierAdministratif;
import com.gestionscolaire.repository.DossierAdministratifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DossierAdministratifService {

    @Autowired
    private DossierAdministratifRepository dossierRepository;

    public List<DossierAdministratif> findAll() {
        return dossierRepository.findAll();
    }

    public Optional<DossierAdministratif> findById(Long id) {
        return dossierRepository.findById(id);
    }

    public Optional<DossierAdministratif> findByNumeroInscription(String numeroInscription) {
        return dossierRepository.findByNumeroInscription(numeroInscription);
    }

    public DossierAdministratif save(DossierAdministratif dossier) {
        return dossierRepository.save(dossier);
    }

    public void deleteById(Long id) {
        dossierRepository.deleteById(id);
    }

    public boolean existsByNumeroInscription(String numeroInscription) {
        return dossierRepository.existsByNumeroInscription(numeroInscription);
    }
}
