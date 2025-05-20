package com.taskhive.backend.repository;

import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import lombok.extern.java.Log;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@Log
@DataJpaTest(properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none",
})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//to ensure the tests are executed in the order mentioned
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InboxRepositoryTest {
    Inbox inbox;
    AppUser user;

    @Autowired
    InboxRepository inboxRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @BeforeEach
    public void init() {
        user = AppUser.builder().email("email@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("newuser").build();

        inbox = Inbox.builder().user(user).pid(9).title(InboxInviteTitle.INVITATION).initiator("initiator").projectName("project").build();


    }

    @Test
    @Order(1)
    //Data from this test is used by following @Test
    @Rollback(false)
    public void saveInbox() {
        //Since Inbox has User object and Inbox owns the relationship with CascadeType.Persist, User object should be saved in the database as well.
        inboxRepository.save(inbox);
    }

    //This @Test is dependent on data saved by saveInbox()
    @Test
    //to ensure that the data is actually removed from the database set by saveInbox()
    @Rollback(false)

    public void InboxRepository_Save_ReturnsSavedInbox() {
        Assertions.assertThat(inboxRepository.count()).isEqualTo(1);
        Assertions.assertThat(appUserRepository.count()).isEqualTo(1);

        AppUser savedUser = appUserRepository.findByUsername(user.getUsername());
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUid()).isGreaterThan(0);
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(savedUser.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(savedUser.getLastName()).isEqualTo(user.getLastName());

        Inbox savedInbox = savedUser.getInboxes().getFirst();
        /*
        The above line returns 'null' although both Inbox and AppUser is saved in the database
        This bug is discussed in /test/Test1.java
         */

        Assertions.assertThat(savedInbox).isNotNull();
        Assertions.assertThat(savedInbox.getInboxId()).isGreaterThan(0);
        Assertions.assertThat(savedInbox.getTitle()).isEqualTo(inbox.getTitle());
        Assertions.assertThat(savedInbox.getPid()).isEqualTo(inbox.getPid());
        Assertions.assertThat(savedInbox.getInitiator()).isEqualTo(inbox.getInitiator());
        Assertions.assertThat(savedInbox.getProjectName()).isEqualTo(inbox.getProjectName());

        //clearing the data set by the saveInbox() method
        appUserRepository.deleteAll();
        inboxRepository.deleteAll();
    }
}
