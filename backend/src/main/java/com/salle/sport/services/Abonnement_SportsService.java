package com.salle.sport.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salle.sport.dto.abonnement_sports.DTO_get_abonnement_sports;
import com.salle.sport.dto.abonnement_sports.DTO_post_abonnement_sports;
import com.salle.sport.dto.abonnement_sports.DTO_put_abonnement_sports;
import com.salle.sport.entites.Abonnement_sports;
import com.salle.sport.infra.Response;
import com.salle.sport.repository.Abonnement_SportsRepository;

@Service
public class Abonnement_SportsService {
    
    @Autowired
    private Abonnement_SportsRepository repository;

    //recuperer tous les abonnement_sports enregistrer
    public Response<DTO_get_abonnement_sports> recuperer_all_abonnement_sports(){ 
        Response<DTO_get_abonnement_sports> response = new Response<>();
        try {
            //convertir la liste d'abonnement_sports reçu en une list de DTO_get_abonnement_sportss
            List<DTO_get_abonnement_sports> abonnement_sport = repository.findAll().stream().map(DTO_get_abonnement_sports::new).toList(); 
            response.success("succes", "OK", abonnement_sport);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

//pour recuperer un abonnement_sport en utilisant l'id
    public Response<DTO_get_abonnement_sports> recuperer_abonnement_sports_id(Long id){
        Response<DTO_get_abonnement_sports> response = new Response<>();
        try {
            Abonnement_sports abonnement_sport = repository.getReferenceById(id);

            List<DTO_get_abonnement_sports> list_abonnement_sports = new ArrayList<>();
            list_abonnement_sports.add(new DTO_get_abonnement_sports(abonnement_sport));

            response.success("Sucess", "OK", list_abonnement_sports);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

    public Response<DTO_get_abonnement_sports> recuperer_sports_by_idAbonnement(Long id_abonnement){
        Response<DTO_get_abonnement_sports> response = new Response<>();
        try {
            List<DTO_get_abonnement_sports> abonnement_sport = repository.findAllByIdAbonnement(id_abonnement).stream().map(DTO_get_abonnement_sports::new).toList();

            response.success("Sucess", "OK", abonnement_sport);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Enregister un nouveau abonnement_sport
    public Response<DTO_get_abonnement_sports> create_abonnement_sports(DTO_post_abonnement_sports data){
        Response<DTO_get_abonnement_sports> response = new Response<>();

        try {
            Abonnement_sports abonnement_sport = new Abonnement_sports(data);
            repository.save(abonnement_sport);
            // crie une list abonnement_sports et l'inicializer pour ajouter les informations de l'abonnement_sport que vien d'etre crie
            List<DTO_get_abonnement_sports> abonnement_sports = new ArrayList<>();
            abonnement_sports.add(new DTO_get_abonnement_sports(abonnement_sport));
            
            response.success("abonnement_sport ajutée", "OK", abonnement_sports);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Modifier les information des abonnement_sports
    public Response<DTO_get_abonnement_sports> modifier_abonnement_sport(DTO_put_abonnement_sports data){
        Response<DTO_get_abonnement_sports> response = new Response<>();
        try {
            Abonnement_sports abonnement_sport = repository.getReferenceById(data.id());
            abonnement_sport.UpdateInfo(data);

            List<DTO_get_abonnement_sports> abonnement_sports = new ArrayList<>();
            abonnement_sports.add(new DTO_get_abonnement_sports(abonnement_sport));
            
            response.success("abonnement_sport modifier", "OK", abonnement_sports);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Supprimer un abonnement_sport
    public Response<DTO_get_abonnement_sports> delete_abonnement_sport(Long id){
        Response<DTO_get_abonnement_sports> response = new Response<>();
        try {
            Abonnement_sports abonnement_sport = repository.getReferenceById(id);
            repository.delete(abonnement_sport);

            response.success("abonnement_sport supprimé", "OK", null);
            
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

}
