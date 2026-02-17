package com.salle.sport.entites;

import com.salle.sport.dto.abonnement_sports.DTO_post_abonnement_sports;
import com.salle.sport.dto.abonnement_sports.DTO_put_abonnement_sports;

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
@Table(name = "abonnement_sports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Abonnement_sports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_sports;
    private Long id_abonnement;
    
    public Abonnement_sports(DTO_post_abonnement_sports data) {
        this.id_sports = data.id_sports();
        this.id_abonnement = data.id_abonnement();
    }

    public void UpdateInfo(DTO_put_abonnement_sports data) {
        if (data.id_sports() != null) this.id_sports = data.id_sports();
        if (data.id_abonnement() != null) this.id_abonnement = data.id_abonnement();
}
}