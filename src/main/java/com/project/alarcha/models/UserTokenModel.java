package com.project.alarcha.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenModel {
    private String token;
    private String refreshtoken;
    private String firstName;
    private String LastName;
    private String email;
    private String phone;
    private String role;
}