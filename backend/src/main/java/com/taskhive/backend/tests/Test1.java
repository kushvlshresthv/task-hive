package com.taskhive.backend.tests;

import com.taskhive.backend.constants.InboxInviteTitle;
import com.taskhive.backend.entity.AppUser;
import com.taskhive.backend.entity.Inbox;
import com.taskhive.backend.repository.AppUserRepository;
import com.taskhive.backend.repository.InboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("/test")
public class Test1 {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    InboxRepository inboxRepository;

    /*

        The following test cases confirms the bug:

        1) when you save an Inbox object which uses User object
        2) both Inbox and User object are saved in the database(confirmed)
        3) However, when immediately retrieving the User object from the database, the List<Inboxes> is not retrieved
        4) I have tried running flush() before retrieving the User object, but it is not working
        5) have not tried clear() yet though

        NOTE: however, retrieving the User object in the new /route loads the List<Inboxes> without any problems.
        NOTE: Before running this test, confirm that User with username "test1_1" is not already in the database
              First route to /test1_1(fails) and then route to /test1_2(passes)
        NOTE: This bug was encountered when writing test cases for InboxRepositoryTests
     */
    @GetMapping("/test1_1")
    public String test1_1() {

        AppUser user = AppUser.builder().email("email@gmail.com").password("nopass").confirmPassword("nopass").firstName("firstName").lastName("lastName").username("test1_1").build();
        Inbox inbox = Inbox.builder().user(user).pid(9).title(InboxInviteTitle.INVITATION).build();

        inboxRepository.save(inbox);
        AppUser savedUser = appUserRepository.findByUsername("test1_1");
        List<Inbox> inboxes = savedUser.getInboxes();

        if (inboxes != null) {
            return "test passed as List<Inbox> is not null in User object : database populated with test data & run /test1_2 to clear test data";
        }
        return "test failed as List<Inbox> is null in User object: database populated with test data";
    }

    @GetMapping("/test1_2")
    public String test1_2() {
        AppUser savedUser = appUserRepository.findByUsername("test1_1");
        List<Inbox> inboxes = savedUser.getInboxes();
        if (inboxes != null) {
            inboxes.clear();
            appUserRepository.save(savedUser);
            appUserRepository.delete(savedUser);
            return "test passed as List<Inbox> is not null in User object: test related data removed from database ";
        }
        return "test failed as List<Inbox> is null in User object: test related data not removed from database; ";
    }

}
