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

import com.salle.sport.dto.sports.DTO_get_sports;
import com.salle.sport.dto.sports.DTO_post_sports;
import com.salle.sport.dto.sports.DTO_put_sports;
import com.salle.sport.infra.Response;
import com.salle.sport.services.SportService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping("/sport")
public class SportsController {
    
    @Autowired
    private SportService sportService;

//recuperer tous les sports
    @GetMapping
    public Response<DTO_get_sports> recuperer_sports(){
        return sportService.get_sports();
    }

// recuperer les sport avec l'ID
    @GetMapping("/{id}/edit")
    public Response<DTO_get_sports> get_by_id(@PathVariable Long id){
        return sportService.get_by_id(id);
    }

//Recuperer le sport avec le nom
    @GetMapping("/{nom}")
    public Response<DTO_get_sports> get_by_nom(@PathVariable String nom){
        return sportService.get_by_nom(nom);
    }

//modifier les info du sport
    @PutMapping
    @Transactional
    public Response<DTO_get_sports> update_sport(@RequestBody @Valid DTO_put_sports data){
        return sportService.update_sport(data);
    }

//sauvegarder un nouveau sport
    @PostMapping
    @Transactional
    public Response<DTO_get_sports> save_sports(@RequestBody @Valid DTO_post_sports data){
        return sportService.save_sport(data);
    }

//suprrimer un sport
    @DeleteMapping("/{id}")
    @Transactional
    public Response<DTO_get_sports> delete_sport(@PathVariable Long id){
        return sportService.delete_sport(id);
    }
}
