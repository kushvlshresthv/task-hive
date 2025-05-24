package com.taskhive.backend.repository;

import com.taskhive.backend.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, Integer> {
    public Inbox findByInboxId(int inboxId);

//    public Inbox findByInboxIdOrderByCreatedDateDesc(int inboxId);
}