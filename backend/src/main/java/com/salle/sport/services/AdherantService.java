package com.salle.sport.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salle.sport.dto.adherants.DTO_list_adherant;
import com.salle.sport.dto.adherants.DTO_post_adherant;
import com.salle.sport.dto.adherants.DTO_put_adherant;
import com.salle.sport.entites.Adherant;
import com.salle.sport.infra.Response;
import com.salle.sport.repository.AdherantRepository;

@Service
public class AdherantService {
    @Autowired
    private AdherantRepository repository;
    
//recuperer tous les adherants enregistrer
    public Response<DTO_list_adherant> recuperer_all_adherants(){ 
        Response<DTO_list_adherant> response = new Response<>();
        try {
            //convertir la liste d'adherants reçu en une list de DTO_list_adherants
            List<DTO_list_adherant> adherents = repository.findActiveAdherants().stream().map(DTO_list_adherant::new).toList(); 
            response.success("succes", "OK", adherents);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

//pour recuperer un adherant en utilisant l'id
    public Response<DTO_list_adherant> recuperer_adherants_id(Long id){
        Response<DTO_list_adherant> response = new Response<>();
        try {
            Adherant adherant = repository.getReferenceById(id);
            List<DTO_list_adherant> adherants = new ArrayList<>();
            adherants.add(new DTO_list_adherant(adherant));

            response.success("Sucess", "OK", adherants);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

//pour recuperer un adherant en utilisant l'CIN ou le nom
    public Response<DTO_list_adherant> detail_adherant(String data){
        Response<DTO_list_adherant> response = new Response<>();
        try {
            List<DTO_list_adherant> adherants = repository.findByCinOrNom(data)
                .stream()
                .map(DTO_list_adherant::new)
                .toList();

            response.success("Sucess", "OK", adherants);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }
//pour recuperer un adherant en utilisant l'CIN ou le nom
    public Response<DTO_list_adherant> Chercher_by_CIN(String cin){
    Response<DTO_list_adherant> response = new Response<>();
    try {
        Adherant adherant = repository.findByCin(cin);

        List<DTO_list_adherant> adherants = new ArrayList<>();
        adherants.add(new DTO_list_adherant(adherant));

        response.success("Sucess", "OK", adherants);

    } catch (Exception e) {
        response.exception(e.getMessage(), "KO");
    }
    return response;
}
// Enregister un nouveau adherant
    public Response<DTO_list_adherant> create_adherants(DTO_post_adherant data){
        Response<DTO_list_adherant> response = new Response<>();
        Adherant adherant1 = repository.findByCin(data.cin());
        //tester si l'adherant existe deja
        if(adherant1 != null){
            String message = "Le CIN "+data.cin()+" est déjà enregistre, vous pouvez chercher le numero de CIN et activer l'adherant";
            response.error(message, "KO");
            return response;
        }else if((repository.findByMail(data.mail()))!= null){
            String message = "Le mail "+data.mail()+" est déjà enregistre, veuillez essayer un nouveau adress mail";
            response.error(message, "KO");
            return response;
        }
        try {
            Adherant adherant = new Adherant(data);
            repository.save(adherant);
            // crie une list Adherants et l'inicializer pour ajouter les informations de l'adherant que vien d'etre crie
            List<DTO_list_adherant> adherants = new ArrayList<>();
            adherants.add(new DTO_list_adherant(adherant));
            
            response.success("Adherant ajutée", "OK", adherants);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Modifier les information des Adherants
    public Response<DTO_list_adherant> modifier_adherant(DTO_put_adherant data){
        Response<DTO_list_adherant> response = new Response<>();
        try {
            Adherant adherant = repository.getReferenceById(data.id());
            adherant.UpdateInfo(data);

            List<DTO_list_adherant> adherants = new ArrayList<>();
            adherants.add(new DTO_list_adherant(adherant));
            
            response.success("Adherant modifier", "OK", adherants);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

// Supprimer un adherant
    public Response<DTO_list_adherant> delete_adherant(Long id){
        Response<DTO_list_adherant> response = new Response<>();
        try {
            Adherant adherant = repository.getReferenceById(id);
            adherant.desactiver();

            List<DTO_list_adherant> adherants = new ArrayList<>();
            adherants.add(new DTO_list_adherant(adherant));

            response.success("Adherant supprimé", "OK", adherants);
            
        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

//activer un adherant
    public Response<DTO_list_adherant> activer_adherant(Long id){
        Response<DTO_list_adherant> response = new Response<>();
        try {
            Adherant adherant = repository.getReferenceById(id);
            adherant.activer();

            List<DTO_list_adherant> adherants = new ArrayList<>();
            adherants.add(new DTO_list_adherant(adherant));

            response.success("Adherant active", "OK", adherants);

        } catch (Exception e) {
            response.exception(e.getMessage(), "KO");
        }
        return response;
    }

    //Nmbre des adherants
    public Response<Long> count_Adherants(){
        Response<Long> response = new Response<>();
        Long nombre =  repository.countAdherants();
        List<Long> list_numbers = new ArrayList<>();
        list_numbers.add(nombre);
        response.success("sucess", "ok", list_numbers);
        return response;
    }
}
