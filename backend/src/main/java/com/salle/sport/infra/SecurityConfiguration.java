package com.salle.sport.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityfilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(management ->
                management.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .authorizeHttpRequests(authorize ->
                authorize
                    //Login
                    .requestMatchers(HttpMethod.POST, "/login").permitAll()
                    //websocket handshake and STOMP endpoints should be reachable (filter will still process them)
                    .requestMatchers("/ws/**").permitAll()
                    .requestMatchers("/app/**", "/topic/**", "/queue/**").permitAll()
                    //tous les autres request
                    .anyRequest().authenticated()
            )
            //para adicionar o nosso filter(securityFilter) a ser usado antes do filter do spring
            .addFilterBefore(
                securityfilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

    //para mostrar para o spring aonde ele deve pegar o AuthenticationManager
    @Bean
    public AuthenticationManager authenticationmanager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //para criptar o password do usuario
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
