package com.taskhive.backend.repository;

import com.taskhive.backend.model.RegisterUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none",
})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    RegisterUser user = RegisterUser.builder().email("ikus shalstha@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").build();

    @Test
    public void RegisterUserRepository_Save_ReturnsSavedUser() {
        RegisterUser savedUser = userRepository.save(user);
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUid()).isGreaterThan(0);
    }

    @Test
    public void RegisterUserRepository_FindByUsername_ReturnsUser() {
        RegisterUser savedUser = userRepository.save(user);
        RegisterUser user = userRepository.findByUsername("newuser");
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUsername()).isEqualTo("newuser");
    }
}
