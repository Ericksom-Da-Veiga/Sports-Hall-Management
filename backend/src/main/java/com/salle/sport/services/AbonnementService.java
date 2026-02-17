package com.salle.sport.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salle.sport.dto.abonnement.DTO_get_abonnement;
import com.salle.sport.dto.abonnement.DTO_post_abonnement;
import com.salle.sport.dto.abonnement.DTO_put_abonnement;
import com.salle.sport.entites.Abonnement;
import com.salle.sport.infra.Response;
import com.salle.sport.repository.AbonnementRepository;


@Service
public class AbonnementService {
    @Autowired
    private AbonnementRepository repository;
    

//recuperer tous les abonnement enregistrer
    public Response<DTO_get_abonnement> recuperer_all_abonnement(){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            List<DTO_get_abonnement> abonnements = repository.findActiveAbonnements().stream().map(DTO_get_abonnement::new).toList();
            response.success("sucess", "OK", abonnements);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }
//pour recuperer un abonnement en utilisant l'id
    public Response<DTO_get_abonnement> recuperer_abonnemnt_by_id(Long id){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            List<DTO_get_abonnement> abonnement = repository.findAbonnementById(id).stream().map(DTO_get_abonnement::new).toList();
            
            response.success("success", "OK", abonnement);
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

//pour recuperer un abonnement en utilisant l'CIN ou le nom
    public Response<DTO_get_abonnement> detail_abonnement(String data){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            List<DTO_get_abonnement> Abonnements = repository.findByCinOrNom(data).stream().map(DTO_get_abonnement::new).toList();
            response.success("Sucess", "OK", Abonnements);
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

    public Response<DTO_get_abonnement> Find_by_CIN(String cin){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            Abonnement abonnement = repository.findByCinOnly(cin);

            List<DTO_get_abonnement> Abonnements = new ArrayList<>();
            Abonnements.add(new DTO_get_abonnement(abonnement));
            
            response.success("Sucess", "OK", Abonnements);
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }


    public Response<DTO_get_abonnement> create_abonnement(DTO_post_abonnement data, Long id_adherant){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            Abonnement abonnement = new Abonnement(data, id_adherant);
            repository.save(abonnement);

            List<DTO_get_abonnement> Abonnements = new ArrayList<>();
            Abonnements.add(new DTO_get_abonnement(abonnement));

            response.success("success", "ok", Abonnements);
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Modifier abonnement
    public Response<DTO_get_abonnement> modifier_abonnement(DTO_put_abonnement data){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            Abonnement abonnement = repository.getReferenceById(data.id());
            abonnement.updateInfo(data);

            List<DTO_get_abonnement> Abonnements = new ArrayList<>();
            Abonnements.add(new DTO_get_abonnement(abonnement));

            response.success("success", "ok", Abonnements);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Suppimer un Abonnement
    public Response<DTO_get_abonnement> delete_abonnement(Long id){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            Abonnement abonnement = repository.getReferenceById(id);
            abonnement.desactiver();

            response.success("Abonnement suprimmè", "ok", null);
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Activer Abonnement
    public Response<DTO_get_abonnement> activer_abonnement(Long id){
        Response<DTO_get_abonnement> response = new Response<>();
        try {
            Abonnement abonnement = repository.getReferenceById(id);
            abonnement.activer();

            response.success("success", "ok", null);
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

}