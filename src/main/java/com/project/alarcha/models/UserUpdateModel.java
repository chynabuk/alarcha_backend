package com.project.alarcha.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateModel {
    private Long id;

    private String firstName;

    private String lastName;

    @Email(message = "incorrect email format.")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,50}$",
            message = "incorrect password format.")
    private String password;

    @Pattern(regexp = "^\\+\\d+ \\d+$", message = "Incorrect phone format.")
    private String phone;

    private String role;
}