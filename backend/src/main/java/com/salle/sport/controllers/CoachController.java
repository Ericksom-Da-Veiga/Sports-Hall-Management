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
import com.salle.sport.dto.coach.DTO_list_coach;
import com.salle.sport.dto.coach.DTO_post_coach;
import com.salle.sport.dto.coach.DTO_put_coach;
import com.salle.sport.infra.Response;
import com.salle.sport.services.CoachService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private CoachService coachService;

    @GetMapping
    public Response<DTO_list_coach> recuperer_coachs(){
        return coachService.recuperer_coachs();
    }
//pour recuperer un coach en utilisant l'id
    @GetMapping("/{id}/edit")
    public Response<DTO_list_coach> recuperer_coach_id(@PathVariable Long id){
        return coachService.recuperer_coach_id(id);
    }

//pour recuperer un Coach en utilisant l'CIN ou le nom
    @GetMapping("/{data}")
    public Response<DTO_list_coach> detail_coach(@PathVariable String data){
        return coachService.detail_coach(data);
    }

// Enregister un nouveau coach
    @PostMapping
    @Transactional
    public Response<DTO_list_coach> create_coacah(@RequestBody @Valid DTO_post_coach data){
       return coachService.create_coach(data);
    }

// Modifier les information des Adherants
    @PutMapping
    @Transactional
    public Response<DTO_list_coach> modifier_coach(@RequestBody @Valid DTO_put_coach data){
        return coachService.modifier_caoch(data);
    }

// Supprimer un adherant
    @DeleteMapping("/{id}")
    @Transactional
    public Response<DTO_list_coach> delete_coach(@PathVariable Long id){
        return coachService.delete_coach(id);
    }
}