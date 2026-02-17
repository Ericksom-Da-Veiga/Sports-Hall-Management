package com.salle.sport.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salle.sport.dto.abonnement.DTO_get_abonnement;
import com.salle.sport.dto.abonnement.DTO_post_abonnement;
import com.salle.sport.dto.abonnement.DTO_put_abonnement;
import com.salle.sport.entites.Abonnement;
import com.salle.sport.entites.Adherant;
import com.salle.sport.infra.Response;
import com.salle.sport.repository.AbonnementRepository;
import com.salle.sport.repository.AdherantRepository;
import com.salle.sport.services.AbonnementService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/abonnements")
public class AbonnementController {
    
    @Autowired
    private AbonnementService service;
    @Autowired
    private AbonnementRepository repository;
    @Autowired
    private AdherantRepository Adherantrepository;


    @GetMapping
    public Response<DTO_get_abonnement> recuperer_all_abonnement(){
        return service.recuperer_all_abonnement();
    }

    @GetMapping("/{id}/edit")
    public Response<DTO_get_abonnement> recuperer_abonnemnt_by_id(@PathVariable Long id){
        return service.recuperer_abonnemnt_by_id(id);
    }

    @GetMapping("/{data}")
    public Response<DTO_get_abonnement> detail_abonnement(@PathVariable String data){
        return service.detail_abonnement(data);
    }

    @GetMapping("/find/{cin}")
    public Response<DTO_get_abonnement> Find_by_CIN(@PathVariable String cin){
        return service.Find_by_CIN(cin);
    }

    @PostMapping
    @Transactional
    public Response<DTO_get_abonnement> create_abonnement(@RequestBody @Valid DTO_post_abonnement data) {
        // Vérifier si l'adhérent a déjà un abonnement
        List<Abonnement> abonnements = repository.findByCinOrNom(data.cin());
        if (!abonnements.isEmpty()) {
            String message = "L'adhérent avec le CIN " + data.cin() + " est déjà inscrit dans un abonnement";
            Response<DTO_get_abonnement> response = new Response<>();
            response.exception(message, "KO");
            return response;
        } else {
            // Récupérer l'adhérent par son CIN
            Adherant adherant = Adherantrepository.findByCin(data.cin());
            //tester si l'adherant existe
            if (adherant != null) {
                return service.create_abonnement(data, adherant.getId());
            } else {
                // Gérer le cas où aucun adhérent n'est trouvé pour le CIN donné
                String message = "Aucun adhérent trouvé avec le CIN " + data.cin();
                Response<DTO_get_abonnement> response = new Response<>();
                response.exception(message, "KO");
                return response;
            }
        }
    }

    @PutMapping
    @Transactional
    public Response<DTO_get_abonnement> modifier_abonnement(@RequestBody @Valid DTO_put_abonnement data){
        return service.modifier_abonnement(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Response<DTO_get_abonnement> delete_abonnement(@PathVariable Long id){
        return service.delete_abonnement(id);
    }

    @PutMapping("/activer/{id}")
    @Transactional
    public Response<DTO_get_abonnement> activer_abonnement(@PathVariable Long id){
        return service.activer_abonnement(id);
    }


}