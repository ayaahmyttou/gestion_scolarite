package com.gestionscolaire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
//@Component
public class Filiere {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String nom;

    //Une filiere est associé à plusieurs élèves
    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL)
    private List<Eleve> eleves = new ArrayList<>();

    //Plusieurs filieres ont plusieurs cours en commun
    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL)
    private List<Cours> cours = new ArrayList<>();

    // Méthode utilitaire pour ajouter un élève
    public void addEleve(Eleve eleve) {
        eleves.add(eleve);
        eleve.setFiliere(this);
    }

    // Méthode utilitaire pour ajouter un cours
    public void addCours(Cours cours) {
        this.cours.add(cours);
        cours.setFiliere(this);
    }

}
