package com.project.alarcha.service.impl;

import com.project.alarcha.config.security.JwtUtils;
import com.project.alarcha.entities.RefreshToken;
import com.project.alarcha.entities.User;
import com.project.alarcha.enums.UserRole;
import com.project.alarcha.enums.UserStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.*;
import com.project.alarcha.repositories.UserRepository;
import com.project.alarcha.service.RefreshTokenService;
import com.project.alarcha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final JwtUtils jwtUtils;

    public UserServiceImpl(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserTokenModel authorization(UserAuthModel userAuthModel) {
        String email = userAuthModel.getEmail();
        String password = userAuthModel.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserSecurityModel userDetails = (UserSecurityModel) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        UserTokenModel userTokenModel = initUserTokenDTO(userDetails, jwtToken, refreshToken);
        return userTokenModel;
    }

    @Override
    public UserToSendModel createUser(UserRegistrationModel userRegistrationModel) {
        checkEmailForUnique(userRegistrationModel.getEmail());
        User user = initAndSaveUser(userRegistrationModel);
        return initUserToSendModel(user);
    }

    @Override
    public List<UserToSendModel> getAllUsersToSendDTO(Integer numberPage, Integer sizePage) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public UserToSendModel deleteUser(Long userId) {
        return null;
    }

    @Override
    public UserToSendModel updateUser(UserUpdateModel userUpdateModel) {
        return null;
    }

    private void checkEmailForUnique(String email) {
        User dataUserByEmail = getByEmail(email);
        if (dataUserByEmail != null)
            throw new ApiFailException(email + " is already existed");
    }

    private User initAndSaveUser(UserRegistrationModel userRegistrationModel) {
        User user = new User();
        user.setFirstName(userRegistrationModel.getFirstName());
        user.setLastName(userRegistrationModel.getLastName());
        String encodingPassword = encoder.encode(userRegistrationModel.getPassword());
        user.setPassword(encodingPassword);
        user.setEmail(userRegistrationModel.getEmail());
        user.setPhone(userRegistrationModel.getPhone());
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserRole(UserRole.CLIENT);
        userRepository.save(user);

        return user;
    }

    private UserToSendModel initUserToSendModel(User user) {
        UserToSendModel userToSendModel = new UserToSendModel();
        userToSendModel.setId(user.getId());
        userToSendModel.setFirstName(user.getFirstName());
        userToSendModel.setLastName(user.getLastName());
        userToSendModel.setEmail(user.getEmail());
        userToSendModel.setPhone(user.getPhone());
        userToSendModel.setRole(user.getUserRole().name());
        return userToSendModel;
    }

    private UserTokenModel initUserTokenDTO(UserSecurityModel userDetails, String accessToken, RefreshToken refreshToken) {
        UserTokenModel userTokenModel = new UserTokenModel();
        userTokenModel.setFirstName(userDetails.getFirstName());
        userTokenModel.setLastName(userDetails.getLastName());
        userTokenModel.setEmail(userDetails.getUsername());
        userTokenModel.setPhone(userTokenModel.getPhone());
        userTokenModel.setRole(userTokenModel.getRole());
        userTokenModel.setToken(accessToken);
        userTokenModel.setRefreshtoken(refreshToken.getToken());
        return userTokenModel;
    }
}