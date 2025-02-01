package com.taskhive.backend.service;

import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.repository.InboxRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


//@SpringBootTest is used to load a particular bean into the ApplicatonContext
@SpringBootTest(classes = {InboxService.class})
public class InboxServiceTest {
    @Autowired
    InboxService inboxService;

    @MockBean
    InboxRepository inboxRepository;

    Inbox inbox;

    @BeforeEach
    public void init() {
        inbox = Inbox.builder().build();
    }

    @Test
    public void InboxService_SaveInbox_ReturnsSavedInbox() {
        Mockito.when(inboxRepository.save(inbox)).thenReturn(inbox);

        Inbox savedInbox = inboxService.saveInbox(inbox);

        Assertions.assertThat(savedInbox).isNotNull();
        Assertions.assertThat(savedInbox).isEqualTo(inbox);
    }
}

