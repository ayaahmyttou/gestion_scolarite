package com.gestionscolaire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String intitule;

    //Plusieurs cours sont suivis par plusieurs eleves
    @ManyToMany(mappedBy = "cours")
    private List<Eleve> eleves = new ArrayList<>();

    //Plusieurs cours sont associés à une filiere
    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private Filiere filiere ;

    // Constructeur pour faciliter la création
    public Cours(String code, String intitule, Filiere filiere) {
        this.code = code;
        this.intitule = intitule;
        this.filiere = filiere;
    }



}
