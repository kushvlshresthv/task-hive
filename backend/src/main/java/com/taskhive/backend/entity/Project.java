package com.taskhive.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskhive.backend.constants.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
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
    @Size(min = 1, max = 50)
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
    @Size(min = 1, max = 20)
    String projectType;

    @NotNull
    String priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    ProjectStatus status;

    //@JsonIgnore because we don't want to send the 'user' object back when returning the project

    //@JsonIgnore will omit the 'user' property from the response body

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false)
    @JsonIgnore
    AppUser user;

    @ManyToMany(mappedBy = "joinedProjects", fetch = FetchType.LAZY)
    @JsonIgnore
    List<AppUser> joinedUsers;
}

