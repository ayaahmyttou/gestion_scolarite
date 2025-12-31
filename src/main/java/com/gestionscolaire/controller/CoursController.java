package com.gestionscolaire.controller;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.model.Filiere;
import com.gestionscolaire.service.CoursService;
import com.gestionscolaire.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cours")
public class CoursController {
    @Autowired
    private CoursService coursService;

    @Autowired
    private FiliereService filiereService;

    @GetMapping
    public String listCours(Model model) {
        model.addAttribute("cours", coursService.findAll());
        return "cours/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("filieres", filiereService.findAll());
        return "cours/form";
    }

    @PostMapping
    public String createCours(@ModelAttribute Cours cours,
                              @RequestParam Long filiereId,
                              RedirectAttributes redirectAttributes) {
        try {
            // Vérifier si le code existe déjà
            if (coursService.existsByCode(cours.getCode())) {
                redirectAttributes.addFlashAttribute("error",
                        "Un cours avec ce code existe déjà");
                return "redirect:/cours/new";
            }

            // Associer la filière
            Filiere filiere = filiereService.findById(filiereId)
                    .orElseThrow(() -> new RuntimeException("Filière non trouvée"));
            cours.setFiliere(filiere);

            coursService.save(cours);
            redirectAttributes.addFlashAttribute("success",
                    "Cours créé avec succès");
            return "redirect:/cours";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la création du cours: " + e.getMessage());
            return "redirect:/cours/new";
        }
    }

    @GetMapping("/{id}")
    public String showCours(@PathVariable Long id, Model model) {
        Cours cours = coursService.findByIdWithEleves(id);
        if (cours == null) {
            return "redirect:/cours";
        }
        model.addAttribute("cours", cours);
        return "cours/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Cours cours = coursService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        model.addAttribute("cours", cours);
        model.addAttribute("filieres", filiereService.findAll());
        return "cours/form";
    }

    @PostMapping("/{id}")
    public String updateCours(@PathVariable Long id,
                              @ModelAttribute Cours cours,
                              @RequestParam Long filiereId,
                              RedirectAttributes redirectAttributes) {
        try {
            // Vérifier si le code existe pour un autre cours
            if (coursService.existsByCodeAndIdNot(cours.getCode(), id)) {
                redirectAttributes.addFlashAttribute("error",
                        "Un autre cours avec ce code existe déjà");
                return "redirect:/cours/" + id + "/edit";
            }

            // Associer la filière
            Filiere filiere = filiereService.findById(filiereId)
                    .orElseThrow(() -> new RuntimeException("Filière non trouvée"));

            cours.setId(id);
            cours.setFiliere(filiere);
            coursService.save(cours);

            redirectAttributes.addFlashAttribute("success",
                    "Cours mis à jour avec succès");
            return "redirect:/cours/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la mise à jour: " + e.getMessage());
            return "redirect:/cours/" + id + "/edit";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCours(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        try {
            coursService.deleteById(id);
            redirectAttributes.addFlashAttribute("success",
                    "Cours supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de supprimer le cours: " + e.getMessage());
        }
        return "redirect:/cours";
    }
}
