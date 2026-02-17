package com.salle.sport.entites;

import java.time.LocalDate;
import java.time.Period;

import com.salle.sport.dto.abonnement.DTO_post_abonnement;
import com.salle.sport.dto.abonnement.DTO_put_abonnement;

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
@Table(name = "abonnement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Abonnement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_adherant;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private Double prix_totale; 
    private int active = 1;
    
    public Abonnement(DTO_post_abonnement data, Long id_abonnement) {
        this.id_adherant = id_abonnement;
        this.date_debut = data.date_debut();
        this.date_fin = data.date_fin();
        this.prix_totale = data.prix_totale();
    }

    public void updateInfo(DTO_put_abonnement data) {
        if(data.id_adherant() != null) this.id_adherant = data.id_adherant();
        if(data.date_debut() != null) this.date_debut = data.date_debut();
        if (data.date_fin() != null) this.date_fin = data.date_fin();
        if (data.prix_totale() != null) this.prix_totale = data.prix_totale();
    }

    public void desactiver() {
        this.active = 0;
    }

    public void activer() {
        this.active = 1;
    }

    public void calculerDureeEtAjouter() {
        // Calcula a diferença em meses entre date_debut e date_fin
        Period period = Period.between(date_debut, date_fin);
        
        // Obtém a quantidade de meses da diferença
        long dureeMois = period.toTotalMonths();
        
        // Adiciona a duração em meses à date_fin
        this.date_fin = this.date_fin.plusMonths(dureeMois);
    }
}