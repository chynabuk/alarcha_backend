package com.project.alarcha.controller;

import com.project.alarcha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public void createUser(){
        userService.createSuperAdmin();
    }

    @GetMapping("/")
    public void getCount(){
        userService.getUsersCount();
    }
}