package com.project.alarcha.service;

import com.project.alarcha.entities.User;
import com.project.alarcha.models.*;

import java.util.List;

public interface UserService {
    UserTokenModel authorization(UserAuthModel userAuthModel);
    UserToSendModel createUser(UserRegistrationModel userRegistrationModel);
    List<UserToSendModel> getAllUsersToSendDTO(Integer numberPage, Integer sizePage);
    User getByUsername(String username);
    User getByEmail(String email);
    User getCurrentUser();
    User getById(Long id);
    UserToSendModel deleteUser(Long userId);
    UserToSendModel updateUser(UserUpdateModel userUpdateModel);
}
