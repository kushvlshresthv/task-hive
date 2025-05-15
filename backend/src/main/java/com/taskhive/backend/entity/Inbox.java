package com.taskhive.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskhive.backend.constants.InboxInviteTitle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_inbox")
@Builder
public class Inbox {


    @Id
    @Column(name = "inbox_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int inbox_id;

    //A Inbox is associated with a particular AppUser's uid which is stored in the 'uid' column of 'inbox' table
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @JsonIgnore
    AppUser user;

    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    InboxInviteTitle title;

    @Column(name = "initiator")
    String initiator;

    @Column(name = "project_name")
    String projectName;

    @Column(name = "pid")
    int pid;

}
