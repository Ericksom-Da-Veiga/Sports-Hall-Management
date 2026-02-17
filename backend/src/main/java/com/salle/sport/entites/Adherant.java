package com.salle.sport.entites;

import java.time.LocalDate;

import com.salle.sport.dto.adherants.DTO_post_adherant;
import com.salle.sport.dto.adherants.DTO_put_adherant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "adherant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Adherant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String mail;
    private String password;
    private String nom;
    private String prenom;
    private LocalDate date_naissance;
    private String adress;
    private String telephone;
    private String ville;
    private String nom_pere;
    private String nom_mere;
    private String tel_parant;
    private int active = 1;

//constructor pour crer un adherant atravers le DTO post_adherant
    public Adherant(DTO_post_adherant data){ 
        this.cin = data.cin();
        this.mail = data.mail();
        this.password = data.password();
        this.nom = data.nom();
        this.prenom = data.prenom();
        this.date_naissance = data.date_naissance();
        this.adress = data.adress();
        this.telephone = data.telephone();
        this.ville = data.ville();
        this.nom_pere = data.nom_pere();
        this.nom_mere = data.nom_mere();
        this.tel_parant = data.tel_parant();
    }

// Methode pour modifier les informations du adherant
    public void UpdateInfo(DTO_put_adherant data){
        if(data.mail()!=null) this.mail = data.mail();
        if(data.password()!=null) this.password = data.password();
        if(data.nom()!=null) this.nom = data.nom();
        if(data.prenom() != null) this.prenom = data.prenom();
        if(data.date_naissance() != null) this.date_naissance = data.date_naissance();
        if(data.adress()!=null) this.adress = data.adress();
        if(data.telephone()!=null) this.telephone = data.telephone();
        if(data.nom_pere()!=null) this.nom_pere = data.nom_pere();
        if(data.nom_mere()!=null) this.nom_mere = data.nom_mere();
        if(data.tel_parant()!=null) this.tel_parant = data.tel_parant();
        if(data.ville() != null) this.ville = data.ville();
    }

//Methodes pour activer et desactiver l'adherant
    public void activer(){
        this.active = 1;
    }
    public void desactiver(){
        this.active = 0;
    }

}
