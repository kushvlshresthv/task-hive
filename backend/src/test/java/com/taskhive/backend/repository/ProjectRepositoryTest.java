package com.taskhive.backend.repository;

import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Project;
import lombok.extern.java.Log;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@Log
@DataJpaTest(properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none",
})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class ProjectRepositoryTest {
    Project project;
    AppUser user;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @BeforeEach
    public void init() {
        user = AppUser.builder().email("email@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").build();

        project = Project.builder().projectName("name").projectDescription("description").startDate(LocalDate.now()).finishDate(LocalDate.of(2025, 2, 12)).priority("high").projectType("business").user(user).build();

        appUserRepository.save(user);
    }

    @Test
    public void ProjectRepository_Save_ReturnsSavedProject() {
        Project savedProject = projectRepository.save(this.project);
        Assertions.assertThat(savedProject).isNotNull();
        Assertions.assertThat(savedProject.getPid()).isGreaterThan(0);
        Assertions.assertThat(savedProject.getUser().getUid()).isGreaterThan(0);
    }
}
