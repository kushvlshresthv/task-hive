package com.taskhive.backend.service;

import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.repository.InboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InboxService {
    @Autowired
    InboxRepository inboxRepository;

    public Inbox saveInbox(Inbox inbox) {
        return inboxRepository.save(inbox);
    }

    public Inbox getInboxById(int id) {
        return inboxRepository.findByInboxId(id);
    }

    public void deleteInbox(Inbox inbox) {
        inboxRepository.delete(inbox);
    }

}
