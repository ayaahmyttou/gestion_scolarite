package com.gestionscolaire.controller;

import com.gestionscolaire.model.Filiere;
import com.gestionscolaire.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/filieres")
public class FiliereController {
    @Autowired
    private FiliereService filiereService;


    @GetMapping
    public String listFilieres(Model model) {
        model.addAttribute("filieres", filiereService.findAll());
        return "filiere/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("filiere", new Filiere());
        return "filiere/form";
    }

    @PostMapping
    public String createFiliere(@ModelAttribute Filiere filiere, RedirectAttributes redirectAttributes) {
        try {
            // Vérifier si le code existe déjà
            if (filiereService.existsByCode(filiere.getCode())) {
                redirectAttributes.addFlashAttribute("error",
                        "Une filière avec ce code existe déjà");
                return "redirect:/filieres/new";
            }

            filiereService.save(filiere);
            redirectAttributes.addFlashAttribute("success",
                    "Filière créée avec succès");
            return "redirect:/filieres";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la création de la filière: " + e.getMessage());
            return "redirect:/filieres/new";
        }
    }

    @GetMapping("/{id}")
    public String showFiliere(@PathVariable Long id, Model model) {
        Filiere filiere = filiereService.findByIdWithDetails(id);
        if (filiere == null) {
            return "redirect:/filieres";
        }
        model.addAttribute("filiere", filiere);
        return "filiere/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Filiere filiere = filiereService.findById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée"));
        model.addAttribute("filiere", filiere);
        return "filiere/form";
    }

    @PostMapping("/{id}")
    public String updateFiliere(@PathVariable Long id, @ModelAttribute Filiere filiere, RedirectAttributes redirectAttributes) {
        try {
            // Vérifier si le code existe pour une autre filière
            if (filiereService.existsByCodeAndIdNot(filiere.getCode(), id)) {
                redirectAttributes.addFlashAttribute("error",
                        "Une autre filière avec ce code existe déjà");
                return "redirect:/filieres/" + id + "/edit";
            }

            filiere.setId(id);
            filiereService.save(filiere);
            redirectAttributes.addFlashAttribute("success",
                    "Filière mise à jour avec succès");
            return "redirect:/filieres/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la mise à jour: " + e.getMessage());
            return "redirect:/filieres/" + id + "/edit";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteFiliere(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            filiereService.deleteById(id);
            redirectAttributes.addFlashAttribute("success",
                    "Filière supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de supprimer la filière: " + e.getMessage());
        }
        return "redirect:/filieres";
    }
}
