package com.project.alarcha.controller;

import com.project.alarcha.models.*;
import com.project.alarcha.service.RefreshTokenService;
import com.project.alarcha.service.UserService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/sign-in")
    public ResponseMessage<UserTokenModel> authorization(@Valid @RequestBody UserAuthModel userAuthModel) {
        return new ResponseMessage<UserTokenModel>().prepareSuccessMessage(userService.authorization(userAuthModel));
    }

    @PostMapping("/sign-up")
    public ResponseMessage<UserToSendModel> saveUser(@Valid @RequestBody UserRegistrationModel userRegistrationModel) {
        return new ResponseMessage<UserToSendModel>().prepareSuccessMessage(userService.createUser(userRegistrationModel));
    }

    @PostMapping("/refreshtoken")
    public ResponseMessage<ResponseEntity<?>> refreshtoken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseMessage<ResponseEntity<?>>().prepareSuccessMessage(refreshTokenService.refreshtoken(refreshTokenRequest));
    }

}