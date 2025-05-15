package com.taskhive.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskhive.backend.validators.annotations.CheckUsernameAvailability;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "registered_users")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int uid;

    @NotEmpty
    @Column(name = "firstname")
    String firstName;

    @NotEmpty
    @Column(name = "lastname")
    String lastName;

    @Column(name = "username")
    @NotEmpty
    @CheckUsernameAvailability
    String username;

    @NotEmpty
    @Email
    @Column(name = "email")
    String email;

    @NotEmpty
    @Column(name = "password")
    String password;

    @NotEmpty
    @Transient
    String confirmPassword;

    //OneToMany relationship with Project.class>user property
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Project> ownedProjects;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "project_members",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "uid")

            },

            inverseJoinColumns = {
                    @JoinColumn(name = "project_id", referencedColumnName = "pid")
            }
    )
    @JsonIgnore
    List<Project> joinedProjects;

    //Inbox is associated with a particular 'AppUser' which is tracked by the 'user' property of Inbox.class
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Inbox> inboxes;
}
