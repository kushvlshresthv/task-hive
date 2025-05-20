package com.taskhive.backend.repository;

import com.taskhive.backend.constants.ProjectStatus;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Project;
import lombok.extern.java.Log;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

@Log
@DataJpaTest(properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none",
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

        project = Project.builder().projectName("name").projectDescription("description").startDate(LocalDate.now()).finishDate(LocalDate.now().plusDays(10)).priority("high").projectType("business").user(user).status(ProjectStatus.PLANNED).build();
        //NOTE: joinedUsers is not populated because Project.java does not own the relationship for joinedUser(List<AppUser>) and can't be saved from ProjectRepository
    }

    @Test
    @Rollback(false)
    @Order(1)
    public void saveProject() {
        //this saves Project object as well as the User object associated with the project
        //Why done this way? Look at /test/Test1.java
        projectRepository.save(project);
    }

    @Order(2)
    @Test
    //to prevent rollback of clearing the database populated by saveProject()
    @Rollback(false)
    public void ProjectRepository_Save_ReturnsSavedProject() {

        Assertions.assertThat(appUserRepository.count()).isEqualTo(1);
        Assertions.assertThat(projectRepository.count()).isEqualTo(1);

        AppUser savedUser = appUserRepository.findByUsername(user.getUsername());

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUid()).isGreaterThan(0);

        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(savedUser.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(savedUser.getLastName()).isEqualTo(user.getLastName());


        List<Project> savedProjects = savedUser.getOwnedProjects();
        Project savedProject = savedProjects.getFirst();

        Assertions.assertThat(savedProject).isNotNull();
        Assertions.assertThat(savedProject.getPid()).isGreaterThan(0);
        Assertions.assertThat(savedProject.getUser().getUid()).isGreaterThan(0);

        Assertions.assertThat(savedProject.getProjectName()).isEqualTo(project.getProjectName());
        Assertions.assertThat(savedProject.getProjectDescription()).isEqualTo(project.getProjectDescription());
        Assertions.assertThat(savedProject.getStartDate()).isEqualTo(project.getStartDate());
        Assertions.assertThat(savedProject.getFinishDate()).isEqualTo(project.getFinishDate());
        Assertions.assertThat(savedProject.getProjectType()).isEqualTo(project.getProjectType());
        Assertions.assertThat(savedProject.getPriority()).isEqualTo(project.getPriority());
        Assertions.assertThat(savedProject.getStatus()).isEqualTo(project.getStatus());

        //clearing data set by saveProject() method
        projectRepository.deleteAll();
        appUserRepository.deleteAll();
    }
}
