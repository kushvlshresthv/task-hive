package com.taskhive.backend.service;

import com.taskhive.backend.entity.Project;
import com.taskhive.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project loadProjectByPid(int pid) {
        return projectRepository.findByPid(pid);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
}
