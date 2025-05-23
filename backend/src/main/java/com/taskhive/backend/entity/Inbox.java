package com.taskhive.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskhive.backend.constants.InboxInviteTitle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_inbox")
@Builder
public class Inbox {
    @Id
    @Column(name = "inbox_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int inboxId;

    //A Inbox is associated with a particular AppUser's uid which is stored in the 'uid' column of 'inbox' table
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
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

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }
}
