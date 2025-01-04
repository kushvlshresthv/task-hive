package com.taskhive.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Entity(name = "projects")
@ToString()
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

    @ManyToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false)
    @JsonIgnore
    AppUser user;
}
