package com.taskhive.backend.service;

import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Project;
import com.taskhive.backend.repository.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ProjectService.class})
public class ProjectServiceTest {
    @MockBean
    ProjectRepository projectRepository;

    Project project;

    @Autowired
    ProjectService projectService;

    @BeforeEach
    public void init() {
        AppUser user = AppUser.builder().build();
        project = Project.builder().build();
    }

    @Test
    public void ProjectServiceTest_CreateProject_ReturnsCreatedProject() {

        when(projectRepository.save(project)).thenReturn(project);
        Project returnedProject = projectService.createProject(project);
        Assertions.assertThat(returnedProject).isNotNull();
    }
}
