package com.taskhive.backend.repository;

import com.taskhive.backend.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Integer> {
    public AppUser findByUsername(String username);
}
