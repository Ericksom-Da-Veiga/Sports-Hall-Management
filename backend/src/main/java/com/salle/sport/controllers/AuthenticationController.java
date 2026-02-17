package com.salle.sport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salle.sport.dto.login.DTO_post_login;
import com.salle.sport.entites.Users;
import com.salle.sport.services.TokenService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private TokenService tokenservice;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity<?> Login(@RequestBody @Valid DTO_post_login data){
        // var token = new UsernamePasswordAuthenticationToken(data.mail(), data.password());
        var UserPassword = new UsernamePasswordAuthenticationToken(data.mail(), data.password());
        var authentication = manager.authenticate(UserPassword);

        return ResponseEntity.ok(tokenservice.GenerateToken((Users) authentication.getPrincipal()));
    };
}
