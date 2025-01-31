package com.taskhive.backend.repository;

import com.taskhive.backend.entity.Inbox;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxRepository extends CrudRepository<Inbox, Integer> {
}
