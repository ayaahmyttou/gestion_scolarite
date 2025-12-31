package com.gestionscolaire.service;

import com.gestionscolaire.model.DossierAdministratif;
import com.gestionscolaire.model.Eleve;
import com.gestionscolaire.model.Filiere;
import com.gestionscolaire.repository.EleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EleveService {
    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private DossierAdministratifService dossierAdministratifService;

    public List<Eleve> findAll() {
        return eleveRepository.findAll();
    }

    public Optional<Eleve> findById(Long id) {
        return eleveRepository.findById(id);
    }

    public Eleve findByIdWithDetails(Long id) {
        return eleveRepository.findByIdWithDetails(id);
    }

    public List<Eleve> findByFiliere(Filiere filiere) {
        return eleveRepository.findByFiliere(filiere);
    }

    public List<Eleve> search(String keyword) {
        return eleveRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword);
    }

    public Eleve save(Eleve eleve) {
        // Si c'est un nouvel élève (sans ID) et qu'il n'a pas de dossier administratif
        if (eleve.getId() == null && eleve.getDossierAdministratif() == null) {
            // Sauvegarder d'abord l'élève pour obtenir un ID
            eleve = eleveRepository.save(eleve);

            // Créer le dossier administratif avec numéro d'inscription
            DossierAdministratif dossier = new DossierAdministratif();
            dossier.setDateCreation(LocalDate.now());
            dossier.setNumeroInscription(genererNumeroInscription(eleve));
            dossier.setEleve(eleve);

            // Sauvegarder le dossier
            dossier = dossierAdministratifService.save(dossier);

            // Associer le dossier à l'élève
            eleve.setDossierAdministratif(dossier);
        }

        return eleveRepository.save(eleve);
    }

    public void deleteById(Long id) {
        eleveRepository.deleteById(id);
    }

    public Long countByFiliere(Filiere filiere) {
        return eleveRepository.countByFiliere(filiere);
    }

    /**
     * Génère un numéro d'inscription au format: FILIERE-ANNEE-ID
     * Exemple: INFO-2025-12
     */
    private String genererNumeroInscription(Eleve eleve) {
        String codeFiliere = (eleve.getFiliere() != null)
                ? eleve.getFiliere().getCode()
                : "GEN";

        int annee = LocalDate.now().getYear();
        Long eleveId = eleve.getId();

        return String.format("%s-%d-%d", codeFiliere, annee, eleveId);
    }

    /**
     * Inscrit un élève à un cours
     */
    public void inscrireCours(Long eleveId, Long coursId) {
        Eleve eleve = findById(eleveId)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));

        // Le cours sera chargé dans le contrôleur
        // Cette méthode sera appelée avec les objets déjà chargés
    }

    /**
     * Désinscrit un élève d'un cours
     */
    public void desinscrireCours(Long eleveId, Long coursId) {
        Eleve eleve = findByIdWithDetails(eleveId);
        if (eleve != null) {
            eleve.getCours().removeIf(c -> c.getId().equals(coursId));
            eleveRepository.save(eleve);
        }
    }
}
