package com.taskhive.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity(name = "projects")
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int pid;

    @NotNull
    @Column(name = "project_name")
    String projectName;

    @Column(name = "project_description")
    String projectDescription;

    @NotNull
    @Column(name = "start_date")
    LocalDate startDate;

    @NotNull
    @Column(name = "finish_date")
    LocalDate finishDate;

    @NotNull
    @Column(name = "project_type")
    String projectType;

    @NotNull
    String priority;

    //@JsonIgnore because we don't want to send the 'user' object back when returning the project

    //@JsonIgnore will omit the 'user' property from the response body

    @ManyToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false)
    @JsonIgnore
    AppUser user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "project_members",
            joinColumns = {
                    @JoinColumn(name = "project_id", referencedColumnName = "pid")

            },

            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "uid")
            }
    )
    @JsonIgnore
    List<AppUser> joinedUsers;
}
