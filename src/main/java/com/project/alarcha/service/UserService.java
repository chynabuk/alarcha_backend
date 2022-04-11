package com.project.alarcha.service;

import com.project.alarcha.entities.User;
import com.project.alarcha.models.TokenModel.UserTokenModel;
import com.project.alarcha.models.UserModel.UserAuthModel;
import com.project.alarcha.models.UserModel.UserRegistrationModel;
import com.project.alarcha.models.UserModel.UserToSendModel;
import com.project.alarcha.models.UserModel.UserUpdateModel;

import java.util.List;

public interface UserService {
    UserTokenModel authorization(UserAuthModel userAuthModel);
    UserToSendModel createUser(UserRegistrationModel userRegistrationModel);
    List<UserToSendModel> getAllUsersToSendModel();
    User getByEmail(String email);
    User getCurrentUser();
    User getById(Long id);
    UserToSendModel deleteUser(Long userId);
    UserToSendModel updateUser(UserUpdateModel userUpdateModel);
    void createSuperAdmin();
}
