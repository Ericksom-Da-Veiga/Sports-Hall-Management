package com.salle.sport.repository;

import java.util.List;

import com.salle.sport.entites.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<Users, Long> {
    
    UserDetails findByMail(String mail);

    @Query("select u from Users u where u.cin = :cin")
    Users findByCin(@NotBlank String cin);

    @Query(value = "CALL findUserByCinOrNom(?1)", nativeQuery = true)
    List<Users> finduserByCinOrNom(String data);
}