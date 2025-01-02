package com.taskhive.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Entity(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int pid;

    @Column(name = "project_name")
    String projectName;

    @Column(name = "project_description")
    String projectDescription;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "finish_date")
    LocalDate finishDate;

    @Column(name = "project_type")
    String projectType;

    String priority;

    @ManyToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false)
    AppUser user;
}
