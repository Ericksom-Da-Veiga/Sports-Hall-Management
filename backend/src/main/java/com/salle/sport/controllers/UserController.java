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

import com.salle.sport.dto.user.DTO_get_user;
import com.salle.sport.dto.user.DTO_post_user;
import com.salle.sport.dto.user.DTO_put_user;
import com.salle.sport.infra.Response;
import com.salle.sport.services.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService service;

    @GetMapping
    public Response<DTO_get_user> recuperer_users(){
        return service.recuperer_user();
    }

//pour recuperer un user en utilisant l'id
    @GetMapping("/{idUser}/edit")
    public Response<DTO_get_user> recuperer_user_by_id(@PathVariable Long idUser){
        return service.recuperer_user_id(idUser);
    }

//pour recuperer un User en utilisant l'CIN ou le nom
    @GetMapping("/{data}")
    public Response<DTO_get_user> detail_user(@PathVariable String data){
        return service.detail_user(data);
    }
// Supprimer un adherant
    @DeleteMapping("/{id}")
    public Response<DTO_get_user> delete_user(@PathVariable Long id){
        return service.delete_user(id);
    }

//crier un nouveau user
    @PostMapping
    public Response<DTO_get_user> create_user(@RequestBody @Valid DTO_post_user data){
        return service.create_user(data);
    }

//Modifier les infos du user
    @PutMapping
    public Response<DTO_get_user> modifier_user(@RequestBody @Valid DTO_put_user data){
        return service.modifier_user(data);
    }
}
