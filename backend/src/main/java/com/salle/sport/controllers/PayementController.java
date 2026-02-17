package com.salle.sport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salle.sport.dto.payement.DTO_get_payements;
import com.salle.sport.dto.payement.DTO_post_payements;
import com.salle.sport.infra.Response;
import com.salle.sport.services.PayementService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payements")
public class PayementController {
    @Autowired
    private PayementService service;


    @GetMapping
    public Response<DTO_get_payements> recuperer_payements(){
        return service.recuperer_payements();
    }

    @GetMapping("/{id}/edit")
    public Response<DTO_get_payements> recuperer_by_id(@PathVariable Long id){
        return service.recuperer_payement_by_id(id);
    }

    @PostMapping
    @Transactional
    public Response<DTO_get_payements> save_payement(@RequestBody @Valid DTO_post_payements data){
        return service.save_payements(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Response<DTO_get_payements> delete(@PathVariable Long id){
        return service.delete(id);
    }

    @GetMapping("/countmonth")
    public Response<Long> countMoneyForCurrentMonth(){
        return service.CountMoneyForCurrentMonth();
    }

    @GetMapping("/countTotal")
    public Response<Long> countTotalMoney(){
        return service.CountMoney();
    }
}
