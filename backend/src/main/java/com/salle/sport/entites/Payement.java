package com.salle.sport.entites;

import java.time.LocalDate;

import com.salle.sport.dto.payement.DTO_post_payements;
import com.salle.sport.dto.payement.DTO_put_payement;

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
@Table(name = "payement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date_payement;
    private Long id_abonnement;
    private Long quant_recu;
    private String cin_adherant;
    private Long rendu;

    public Payement(DTO_post_payements data){
        this.date_payement = data.date_payement();
        this.id_abonnement = data.id_abonnement();
        this.quant_recu = data.quant_recu();
        this.cin_adherant = data.cin_adherant();
        this.rendu = data.rendu();
    }

    public void updateInfo(DTO_put_payement data){
        if(data.date_payement() != null) this.date_payement = data.date_payement();
        if(data.id_abonnement() != null) this.id_abonnement = data.id_abonnement();
        if(data.quant_recu() != null) this.quant_recu = data.quant_recu();
    }
}