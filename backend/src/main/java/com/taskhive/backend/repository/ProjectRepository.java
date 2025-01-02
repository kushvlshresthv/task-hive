package com.taskhive.backend.repository;

import com.taskhive.backend.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
   
}
