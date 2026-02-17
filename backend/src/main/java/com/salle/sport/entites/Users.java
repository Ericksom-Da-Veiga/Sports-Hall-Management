package com.salle.sport.entites;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.salle.sport.dto.user.DTO_post_user;
import com.salle.sport.dto.user.DTO_put_user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String mail;
    private String password;
    private String nom;
    private String prenom;
    private String telephone;
    private String role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role.equalsIgnoreCase("ADMIN")) 
			return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN") );
		else 
			return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
    @Override
    public String getUsername() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    //Modifier les info du user
    public void UpdatInfo(DTO_put_user data) {
       if (data.cin() != null) this.cin = data.cin();
       if (data.mail() != null) this.mail = data.mail();
       if (data.nom() != null) this.nom = data.nom();
       if (data.prenom() != null) this.prenom = data.prenom();
       if (data.role() != null) this.role = data.role();
       if (data.password() != null) {
        String ecryptedPassword = new BCryptPasswordEncoder().encode(data.password()); 
        this.password = ecryptedPassword;
       }
    }
//crier un nouveau user
    public Users(DTO_post_user data, String password) {
        this.cin = data.cin();
        this.password = password;
        this.mail = data.mail();
        this.nom = data.nom();
        this.prenom = data.prenom();
        this.role = data.role();
        this.telephone = data.telephone();
    }
}
