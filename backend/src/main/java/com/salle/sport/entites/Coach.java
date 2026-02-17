package com.salle.sport.entites;

import java.time.LocalDate;

import com.salle.sport.dto.coach.DTO_post_coach;
import com.salle.sport.dto.coach.DTO_put_coach;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coach")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String mail;
    private String password;
    private String nom;
    private String prenom;
    private LocalDate date_entree;
    private String adress;
    private String telephone;
    private int idsport;


//constructor pour crer un adherant atravers le DTO post_adherant
    public Coach(DTO_post_coach data){
        this.cin = data.cin();
        this.mail = data.mail();
        this.password = data.password();
        this.nom = data.nom();
        this.prenom = data.prenom();
        this.date_entree = data.date_entree();
        this.adress = data.adress();
        this.telephone = data.telephone();
        this.idsport = data.idsport();
    }

// Methode pour modifier les informations du Coach
    public void UpdatInfo(@Valid DTO_put_coach data) {
        if(data.cin()!=null) this.cin = data.cin();
        if(data.mail()!=null) this.mail = data.mail();
        if(data.password()!=null) this.password = data.password();
        if(data.nom()!=null) this.nom = data.nom();
        if(data.prenom() != null) this.prenom = data.prenom();
        if(data.date_entree() != null) this.date_entree = data.date_entree();
        if(data.adress()!=null) this.adress = data.adress();
        if(data.telephone()!=null) this.telephone = data.telephone();
        this.idsport = data.idsport();
    }
}
