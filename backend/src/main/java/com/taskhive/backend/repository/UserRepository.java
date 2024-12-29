package com.taskhive.backend.repository;

import com.taskhive.backend.model.RegisterUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<RegisterUser, Integer> {
    public RegisterUser findByUsername(String username);
}
