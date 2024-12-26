package com.taskhive.backend.model;

import com.taskhive.backend.validators.annotations.CheckUsernameAvailability;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "registered_users")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterUser {
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
}
