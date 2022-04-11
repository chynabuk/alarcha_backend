package com.project.alarcha.controller;

import com.project.alarcha.models.TokenModel.RefreshTokenRequest;
import com.project.alarcha.models.TokenModel.UserTokenModel;
import com.project.alarcha.models.UserModel.UserAuthModel;
import com.project.alarcha.models.UserModel.UserRegistrationModel;
import com.project.alarcha.models.UserModel.UserToSendModel;
import com.project.alarcha.models.UserModel.UserUpdateModel;
import com.project.alarcha.service.RefreshTokenService;
import com.project.alarcha.service.UserService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
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

    @DeleteMapping("/delete/{userId}")
    public ResponseMessage<UserToSendModel> deleteUser(@PathVariable(name = "userId") Long userId) {
        return new ResponseMessage<UserToSendModel>().prepareSuccessMessage(userService.deleteUser(userId));
    }

    @PutMapping("/update")
    public ResponseMessage<UserToSendModel> saveUser(@Valid @RequestBody UserUpdateModel userUpdateModel) {
        return new ResponseMessage<UserToSendModel>().prepareSuccessMessage(userService.updateUser(userUpdateModel));
    }

    @PostMapping("/refreshtoken")
    public ResponseMessage<ResponseEntity<?>> refreshtoken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseMessage<ResponseEntity<?>>().prepareSuccessMessage(refreshTokenService.refreshtoken(refreshTokenRequest));
    }

}