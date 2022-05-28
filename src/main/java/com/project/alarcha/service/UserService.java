package com.project.alarcha.service;

import com.project.alarcha.entities.User;
import com.project.alarcha.models.TokenModel.UserTokenModel;
import com.project.alarcha.models.UserModel.*;

import java.util.List;

public interface UserService {
    UserTokenModel authorization(UserAuthModel userAuthModel);
    UserToSendModel createUser(UserRegistrationModel userRegistrationModel);
    List<UserToSendModel> getAllUsersToSendModel(int page);
    User getByEmail(String email);
    User getCurrentUser();
    User getById(Long id);
    List<UserToSendModel> getAdmins();
    UserToSendModel deleteUser(Long userId);
    UserToSendModel updateUser(UserUpdateModel userUpdateModel);
    void createSuperAdmin();
    UserOrdersModel getUserOrdersModel(Long id, int page);
}
