package com.salle.sport.repository;

import com.salle.sport.dto.user.DTO_post_user;
import com.salle.sport.entites.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UsersRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    void findByCin() {
        String cin = "A0099819L" ;
        DTO_post_user userDto = new DTO_post_user(cin,"ur@user.com","teste","user","user","07398132","USER");
        this.create_user(userDto);

        Users usersFounded = userRepository.findByCin(cin);
        assertThat(usersFounded).isNotNull();
    }

    @Test
    void finduserByCinOrNom() {
    }

    private Users create_user(DTO_post_user user_dto){
        Users users = new Users(user_dto, user_dto.password());
        this.testEntityManager.persist(users);
        return users;
    }
}