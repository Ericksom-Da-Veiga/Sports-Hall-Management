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

    /**
     * We don't want to execute the JWT validation on websocket handshake endpoints,
     * since SockJS will call extra paths like `/ws/info` and they may not carry
     * a token or could send an invalid one. Let the security configuration handle
     * access rules instead (those paths are already `permitAll`).
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/ws") || path.equals("/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = this.recoverToken(request);
        // if there is a bearer token we attempt to validate it; invalid tokens should
        // not abort the request with an exception, otherwise a 500 is returned to the
        // caller (the websocket handshake is particularly sensitive to this).
        if (token != null) {
            try {
                var subject = tokenservice.getSubjet(token);
                UserDetails user = repository.findByMail(subject);

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // user not found; continue without authentication
                    System.out.println("Usuario nao encontrado por email");
                }
            } catch (RuntimeException ex) {
                // token verification failed (e.g. expired or malformed) - ignore and
                // proceed without setting authentication. Logging might help during
                // development.
                System.out.println("Invalid JWT token during filter: " + ex.getMessage());
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
