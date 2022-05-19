package com.project.alarcha.models.TokenModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenModel {
    private Long userId;
    private String token;
    private String refreshtoken;
    private String firstName;
    private String LastName;
    private String email;
    private String phone;
    private String role;
}