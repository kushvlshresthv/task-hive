package com.taskhive.backend.repository;

import com.taskhive.backend.entity.AppUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none",
})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AppUserRepositoryTests {
    @Autowired
    AppUserRepository appUserRepository;

    AppUser user = AppUser.builder().email("ikushalstha@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").build();

    @Test
    public void RegisterUserRepository_Save_ReturnsSavedUser() {
        appUserRepository.save(user);
        Assertions.assertThat(appUserRepository.count()).isEqualTo(1);
        AppUser savedUser = appUserRepository.findByUsername(user.getUsername());
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUid()).isGreaterThan(0);
        Assertions.assertThat(savedUser).isEqualTo(user);
    }

    @Test
    public void RegisterUserRepository_FindByUsername_ReturnsUser() {
        appUserRepository.save(user);

        Assertions.assertThat(appUserRepository.count()).isEqualTo(1);
        AppUser savedUser = appUserRepository.findByUsername(user.getUsername());
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUid()).isGreaterThan(0);
        Assertions.assertThat(savedUser).isEqualTo(user);
    }
}
