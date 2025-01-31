package com.taskhive.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_inbox")
public class Inbox {


    @Id
    @Column(name = "inbox_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int inbox_id;

    //A Inbox is associated with a particular AppUser's uid which is stored in the 'uid' column of 'inbox' table
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @JsonIgnore
    AppUser user;

    @Column(name = "title")
    String title;

    @Column(name = "message")
    String message;

    @Column(name = "pid")
    int pid;

}
