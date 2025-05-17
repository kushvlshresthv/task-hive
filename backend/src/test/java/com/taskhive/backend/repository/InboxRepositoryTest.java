package com.taskhive.backend.repository;

import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import lombok.extern.java.Log;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Log
@DataJpaTest(properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none",
})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class InboxRepositoryTest {
    Inbox inbox;
    AppUser user;

    @Autowired
    InboxRepository inboxRepository;


    @BeforeEach
    public void init() {
        user = AppUser.builder().email("email@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").build();

        inbox = Inbox.builder().user(user).pid(9).title(InboxInviteTitle.INVITATION).build();
    }

    @Test
    public void InboxRepository_Save_ReturnsSavedInbox() {
        Inbox savedInbox = inboxRepository.save(inbox);
        Assertions.assertThat(savedInbox).isNotNull();
        Assertions.assertThat(savedInbox.getInboxId()).isGreaterThan(0);
        Assertions.assertThat(savedInbox.getUser().getUid()).isGreaterThan(0);
        Assertions.assertThat(savedInbox.getPid()).isEqualTo(9);
    }
}
