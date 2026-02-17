package com.salle.sport.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salle.sport.dto.coach.DTO_list_coach;
import com.salle.sport.dto.coach.DTO_post_coach;
import com.salle.sport.dto.coach.DTO_put_coach;
import com.salle.sport.entites.Coach;
import com.salle.sport.infra.Response;
import com.salle.sport.repository.CoachRepository;


@Service
public class CoachService {
    
    @Autowired
    private CoachRepository repository;

//pour recuperer tous les coachs
    public Response<DTO_list_coach> recuperer_coachs(){
        Response<DTO_list_coach> response = new Response<>();
        try {
            List<DTO_list_coach> coachs = repository.findAll().stream().map(DTO_list_coach::new).toList();
            response.success("success", "OK", coachs);
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }
//pour recuperer un coach en utilisant l'id
    public Response<DTO_list_coach> recuperer_coach_id(Long id){
        Response<DTO_list_coach> response = new Response<>();
        try {
            Coach coach = repository.getReferenceById(id);
            List<DTO_list_coach> coachs = new ArrayList<>();
            coachs.add(new DTO_list_coach(coach));

            response.success("sucess", "OK", coachs);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

//pour recuperer un Coach en utilisant l'CIN ou le nom
    public Response<DTO_list_coach> detail_coach(String data){
        Response<DTO_list_coach> response = new Response<>();
        try {
            List<DTO_list_coach> Coachs = repository.findCoachByCinOrNom(data)
                .stream()
                .map(DTO_list_coach::new)
                .toList();

            response.success("Sucess", "OK", Coachs);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Enregister un nouveau coach
    public Response<DTO_list_coach> create_coach(DTO_post_coach data){
        Response<DTO_list_coach> response = new Response<>();
        Coach coach1 = repository.findByCin(data.cin());
        //tester si le coach existe deja
        if(coach1 != null){
            String message = "Le CIN "+data.cin()+" est déjà enregistre";
            response.error(message, "KO");
            return response;
        }else if((repository.findByMail(data.mail()))!= null){
            String message = "Le mail "+data.mail()+" est déjà enregistre, veuillez essayer un nouveau adress mail";
            response.error(message, "KO");
            return response;
        }
        try {
            Coach coach = new Coach(data);
            repository.save(coach);
            // crie une list Coahs et l'inicializer pour ajouter les informations de l'adherant que vien d'etre crie
            List<DTO_list_coach> coachs = new ArrayList<>();
            coachs.add(new DTO_list_coach(coach));
            
            response.success("Coach ajoutée", "OK", coachs);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Modifier les information des Adherants
    public Response<DTO_list_coach> modifier_caoch(DTO_put_coach data){
        Response<DTO_list_coach> response = new Response<>();
        try {
            Coach coach = repository.getReferenceById(data.id());
            coach.UpdatInfo(data);

            List<DTO_list_coach> coachs = new ArrayList<>();
            coachs.add(new DTO_list_coach(coach));
            
            response.success("Coach modifiè", "OK", coachs);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Supprimer un adherant
    public Response<DTO_list_coach> delete_coach(Long id){
        Response<DTO_list_coach> response = new Response<>();
        try {
            Coach coach = repository.getReferenceById(id);
            repository.delete(coach);

            response.success("Coach supprimè", "OK", null);
            
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }
}
