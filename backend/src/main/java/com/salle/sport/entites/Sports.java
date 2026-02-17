package com.salle.sport.entites;

import com.salle.sport.dto.sports.DTO_post_sports;
import com.salle.sport.dto.sports.DTO_put_sports;

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
@Table(name = "sports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Sports {
   
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Long nmbr_max_seance_semaine;
    private Double prix;
    
    
    public Sports(DTO_post_sports data) {
        this.nom = data.nom();
        this.nmbr_max_seance_semaine =data.nmbr_max_seance_semaine();
        this.prix = data.prix();
    }


    public void updateInfo(DTO_put_sports data) {
        if(data.nom()!=null) this.nom = data.nom();
        if(data.nmbr_max_seance_semaine()!=null) this.nmbr_max_seance_semaine = data.nmbr_max_seance_semaine();
        if(data.prix()!=null) this.prix = data.prix();
    }
}
