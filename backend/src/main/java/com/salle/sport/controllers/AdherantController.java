package com.salle.sport.controllers;

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

import com.salle.sport.dto.adherants.DTO_list_adherant;
import com.salle.sport.dto.adherants.DTO_post_adherant;
import com.salle.sport.dto.adherants.DTO_put_adherant;
import com.salle.sport.infra.Response;
import com.salle.sport.services.AdherantService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/adherants")
public class AdherantController {

    @Autowired
    private AdherantService adherantService;

  
//recuperer tous les adherants enregistrer
    @GetMapping
    public Response<DTO_list_adherant> recuperer_all_adherants(){ 
       return adherantService.recuperer_all_adherants();
    }
    
//pour recuperer un adherant en utilisant l'id
    @GetMapping("/{id}/edit")
    public Response<DTO_list_adherant> recuperer_adherants_id(@PathVariable Long id){
        return adherantService.recuperer_adherants_id(id);
    }

//pour recuperer un adherant en utilisant l'CIN ou le nom
    @GetMapping("/{data}")
    public Response<DTO_list_adherant> detail_adherant(@PathVariable String data){
        return adherantService.detail_adherant(data);
    }

// recuperer un adherant avec le CIN
    @GetMapping("/get/{cin}")
    public Response<DTO_list_adherant> Chercher_by_CIN(@PathVariable String cin){
        return adherantService.Chercher_by_CIN(cin);
    }

// Enregister un nouveau adherant
    @PostMapping
    @Transactional
    public Response<DTO_list_adherant> create_adherants(@RequestBody @Valid DTO_post_adherant data){
        return adherantService.create_adherants(data);
    }

// Modifier les information des Adherants
    @PutMapping
    @Transactional
    public Response<DTO_list_adherant> modifier_adherant(@RequestBody @Valid DTO_put_adherant data){
       return adherantService.modifier_adherant(data);
    }

// Supprimer un adherant
    @DeleteMapping("/{id}")
    @Transactional
    public Response<DTO_list_adherant> delete_adherant(@PathVariable Long id){
       return adherantService.delete_adherant(id);
    }

//activer un adherant
    @PutMapping("/activer/{id}")
    @Transactional
    public Response<DTO_list_adherant> activer_adherant(@PathVariable Long id){
       return adherantService.activer_adherant(id);
    }
}
