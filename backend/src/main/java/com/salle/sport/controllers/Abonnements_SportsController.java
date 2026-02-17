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

import com.salle.sport.dto.abonnement_sports.DTO_get_abonnement_sports;
import com.salle.sport.dto.abonnement_sports.DTO_post_abonnement_sports;
import com.salle.sport.dto.abonnement_sports.DTO_put_abonnement_sports;
import com.salle.sport.infra.Response;
import com.salle.sport.services.Abonnement_SportsService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/abonnement_sports")
public class Abonnements_SportsController {
    
    @Autowired
    private Abonnement_SportsService service;

    @GetMapping
    public Response<DTO_get_abonnement_sports> recuperer_all_abonnement(){
        return service.recuperer_all_abonnement_sports();
    }

    @GetMapping("/{id}/edit")
    public Response<DTO_get_abonnement_sports> recuperer_abonnemnt_by_id(@PathVariable Long id){
        return service.recuperer_abonnement_sports_id(id);
    }

    @GetMapping("/{id_abonnement}")
    public Response<DTO_get_abonnement_sports> recuperer_sports_by_idabonnemengt(@PathVariable Long id_abonnement){
        return service.recuperer_sports_by_idAbonnement(id_abonnement);
    }

    @PostMapping
    @Transactional
    public Response<DTO_get_abonnement_sports> create_abonnement(@RequestBody @Valid DTO_post_abonnement_sports data){
        return service.create_abonnement_sports(data);
    }

    @PutMapping
    @Transactional
    public Response<DTO_get_abonnement_sports> modifier_abonnement(@RequestBody @Valid DTO_put_abonnement_sports data){
        return service.modifier_abonnement_sport(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Response<DTO_get_abonnement_sports> delete_abonnement(@PathVariable Long id){
        return service.delete_abonnement_sport(id);
    }
}