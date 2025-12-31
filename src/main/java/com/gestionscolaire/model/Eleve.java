package com.gestionscolaire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Eleve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    //Plusieurs eleves sont associés à plusieurs cours
    @ManyToMany
    @JoinTable(
            name = "eleve_cours",
            joinColumns = @JoinColumn(name = "eleve_id"),
            inverseJoinColumns = @JoinColumn(name = "cours_id")
    )
    private List<Cours> cours = new ArrayList<>();

    //Plusieurs eleves sont associés à une seule filiere
    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    //Un eleve a son propre dossier administratif
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name= "dossier_administratif_id", unique=true)
    private DossierAdministratif dossierAdministratif;

    // Méthode utilitaire pour inscrire à un cours
    public void inscrireCours(Cours cours) {
        this.cours.add(cours);
        cours.getEleves().add(this);
    }

    // Méthode utilitaire pour se désinscrire d'un cours
    public void desinscrireCours(Cours cours) {
        this.cours.remove(cours);
        cours.getEleves().remove(this);
    }

    // Constructeur pour faciliter la création
    public Eleve(String nom, String prenom, Filiere filiere) {
        this.nom = nom;
        this.prenom = prenom;
        this.filiere = filiere;
    }

    // Obtenir le nom complet
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
