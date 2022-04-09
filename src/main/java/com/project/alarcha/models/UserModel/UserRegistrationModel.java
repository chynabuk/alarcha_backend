package com.project.alarcha.models.UserModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationModel {
    @NotEmpty(message = "First name is required.")
    private String firstName;

    @NotEmpty(message = "Last name is required.")
    private String lastName;

    @NotEmpty(message = "Email is required.")
    @Email(message = "incorrect email format.")
    private String email;

    @NotEmpty(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,50}$",
            message = "incorrect password format.")
    private String password;

    @Pattern(regexp = "^\\+\\d+ \\d+$", message = "Incorrect phone format.")
    private String phone;

    private String role;
}