package com.project.alarcha.controller;

import com.project.alarcha.models.TokenModel.RefreshTokenRequest;
import com.project.alarcha.models.TokenModel.UserTokenModel;
import com.project.alarcha.models.UserModel.*;
import com.project.alarcha.service.RefreshTokenService;
import com.project.alarcha.service.UserService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/get-admins")
    public ResponseMessage<List<UserToSendModel>> getAdmins() {
        return new ResponseMessage<List<UserToSendModel>>().prepareSuccessMessage(userService.getAdmins());
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<UserToSendModel>> getAllUsers(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<UserToSendModel>>().prepareSuccessMessage(userService.getAllUsersToSendModel(page));
    }

    @GetMapping("/get/my-orders/{id}")
    public ResponseMessage<UserOrdersModel> getMyOrders(@PathVariable Long id){
        return new ResponseMessage<UserOrdersModel>().prepareSuccessMessage(userService.getUserOrdersModel(id));
    }

}