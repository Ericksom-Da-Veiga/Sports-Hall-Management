package com.salle.sport.infra;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.salle.sport.repository.UserRepository;
import com.salle.sport.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter  extends OncePerRequestFilter{

     @Autowired
    private TokenService tokenservice;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token =this.recoverToken(request);
        //tester si le token existe on retourne le subjet
        if(token != null){
            var subject = tokenservice.getSubjet(token);

            UserDetails user = repository.findByMail(subject);

            if(user != null){
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                System.out.println("Usuario nao encontrado por email");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
      var authorizationHeader =  request.getHeader("Authorization");
      if (authorizationHeader != null) {
        return authorizationHeader.replace("Bearer ", "");
      }
      return null;
    }
    
}
