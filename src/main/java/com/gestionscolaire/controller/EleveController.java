package com.gestionscolaire.controller;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.model.Eleve;
import com.gestionscolaire.model.Filiere;
import com.gestionscolaire.service.CoursService;
import com.gestionscolaire.service.EleveService;
import com.gestionscolaire.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/eleves")
public class EleveController {
    @Autowired
    private EleveService eleveService;

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private CoursService coursService;

    @GetMapping
    public String listEleves(@RequestParam(required = false) String search, Model model) {
        List<Eleve> eleves;
        if (search != null && !search.trim().isEmpty()) {
            eleves = eleveService.search(search);
            model.addAttribute("search", search);
        } else {
            eleves = eleveService.findAll();
        }
        model.addAttribute("eleves", eleves);
        return "eleve/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("eleve", new Eleve());
        model.addAttribute("filieres", filiereService.findAll());
        return "eleve/form";
    }

    @PostMapping
    public String createEleve(@ModelAttribute Eleve eleve,
                              @RequestParam(required = false) Long filiereId,
                              RedirectAttributes redirectAttributes) {
        try {
            // Associer la filière si fournie
            if (filiereId != null) {
                Filiere filiere = filiereService.findById(filiereId)
                        .orElseThrow(() -> new RuntimeException("Filière non trouvée"));
                eleve.setFiliere(filiere);
            }

            // Le service va créer automatiquement le dossier administratif
            eleveService.save(eleve);

            redirectAttributes.addFlashAttribute("success",
                    "Élève créé avec succès. Dossier administratif généré automatiquement.");
            return "redirect:/eleves";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la création de l'élève: " + e.getMessage());
            return "redirect:/eleves/new";
        }
    }

    @GetMapping("/{id}")
    public String showEleve(@PathVariable Long id, Model model) {
        Eleve eleve = eleveService.findByIdWithDetails(id);
        if (eleve == null) {
            return "redirect:/eleves";
        }

        // Récupérer tous les cours de la filière de l'élève pour l'inscription
        List<Cours> coursDisponibles = null;
        if (eleve.getFiliere() != null) {
            coursDisponibles = coursService.findByFiliere(eleve.getFiliere());
            // Retirer les cours auxquels l'élève est déjà inscrit
            coursDisponibles.removeAll(eleve.getCours());
        }

        model.addAttribute("eleve", eleve);
        model.addAttribute("coursDisponibles", coursDisponibles);
        return "eleve/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Eleve eleve = eleveService.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève non trouvé"));
        model.addAttribute("eleve", eleve);
        model.addAttribute("filieres", filiereService.findAll());
        return "eleve/form";
    }

    @PostMapping("/{id}")
    public String updateEleve(@PathVariable Long id,
                              @ModelAttribute Eleve eleve,
                              @RequestParam(required = false) Long filiereId,
                              RedirectAttributes redirectAttributes) {
        try {
            Eleve existingEleve = eleveService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Élève non trouvé"));

            // Mettre à jour les informations de base
            existingEleve.setNom(eleve.getNom());
            existingEleve.setPrenom(eleve.getPrenom());

            // Mettre à jour la filière si fournie
            if (filiereId != null) {
                Filiere filiere = filiereService.findById(filiereId)
                        .orElseThrow(() -> new RuntimeException("Filière non trouvée"));
                existingEleve.setFiliere(filiere);
            } else {
                existingEleve.setFiliere(null);
            }

            eleveService.save(existingEleve);

            redirectAttributes.addFlashAttribute("success",
                    "Élève mis à jour avec succès");
            return "redirect:/eleves/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la mise à jour: " + e.getMessage());
            return "redirect:/eleves/" + id + "/edit";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteEleve(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        try {
            eleveService.deleteById(id);
            redirectAttributes.addFlashAttribute("success",
                    "Élève supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de supprimer l'élève: " + e.getMessage());
        }
        return "redirect:/eleves";
    }

    @PostMapping("/{eleveId}/inscrire-cours")
    public String inscrireCours(@PathVariable Long eleveId,
                                @RequestParam Long coursId,
                                RedirectAttributes redirectAttributes) {
        try {
            Eleve eleve = eleveService.findByIdWithDetails(eleveId);
            Cours cours = coursService.findById(coursId)
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

            // Vérifier que le cours fait partie de la filière de l'élève
            if (eleve.getFiliere() == null ||
                    !cours.getFiliere().getId().equals(eleve.getFiliere().getId())) {
                redirectAttributes.addFlashAttribute("error",
                        "Le cours ne fait pas partie de la filière de l'élève");
                return "redirect:/eleves/" + eleveId;
            }

            // Inscrire l'élève au cours
            eleve.inscrireCours(cours);
            eleveService.save(eleve);

            redirectAttributes.addFlashAttribute("success",
                    "Élève inscrit au cours avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de l'inscription: " + e.getMessage());
        }
        return "redirect:/eleves/" + eleveId;
    }

    @GetMapping("/{eleveId}/desinscrire-cours/{coursId}")
    public String desinscrireCours(@PathVariable Long eleveId,
                                   @PathVariable Long coursId,
                                   RedirectAttributes redirectAttributes) {
        try {
            eleveService.desinscrireCours(eleveId, coursId);
            redirectAttributes.addFlashAttribute("success",
                    "Élève désinscrit du cours avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la désinscription: " + e.getMessage());
        }
        return "redirect:/eleves/" + eleveId;
    }
}
