package com.salle.sport.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.salle.sport.entites.Users;

@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;
    
    //Pour generer le token
    public String GenerateToken(Users users){
        try {
             Algorithm algorithm = Algorithm.HMAC256(secret);
             String token = JWT.create()
                                .withIssuer("Gym-API")
                                .withSubject(users.getMail())
                                .withClaim("role", users.getRole())
                                .withExpiresAt(genExepirationDate())
                                .sign(algorithm);
                return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error pendant la generation du token",e);
        }
    }

    //pour recuperer l'user en utilisant le token
    public String getSubjet(String tokenJWT){
         try {
             var algorithm = Algorithm.HMAC256(secret);
                return JWT.require(algorithm)
                        .withIssuer("Gym-API")
                        .build()
                        .verify(tokenJWT)
                        .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("invalid token");
        }
    }

    // para determinar o tempo de vida(duraçao) desse nosso token
    private Instant genExepirationDate(){
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("+01:00")); //o nosso token vai ter um tempo de vida de 2 hrs
    }
}
