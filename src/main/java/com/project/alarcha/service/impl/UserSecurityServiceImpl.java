package com.project.alarcha.service.impl;

import com.project.alarcha.entities.User;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.RoleSecurityModel;
import com.project.alarcha.models.UserSecurityModel;
import com.project.alarcha.repositories.UserRepository;
import com.project.alarcha.service.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserSecurityServiceImpl implements UserSecurityService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApiFailException("incorrect email or password"));
        return toUserSecurityModel(user);
    }

    private UserSecurityModel toUserSecurityModel(User user){
        UserSecurityModel userSecurityModel = new UserSecurityModel();
        userSecurityModel.setId(user.getId());
        userSecurityModel.setEmail(user.getEmail());
        userSecurityModel.setPassword(user.getPassword());
        userSecurityModel.setPhone(user.getPhone());

        Set<RoleSecurityModel> roleSecurityModels  = new HashSet<>();

        RoleSecurityModel roleSecurityModel = new RoleSecurityModel();
        roleSecurityModel.setUserSecurityModel(userSecurityModel);
        roleSecurityModel.setRoleName(user.getUserRole().name());

        roleSecurityModels.add(roleSecurityModel);

        return userSecurityModel;
    }
}
