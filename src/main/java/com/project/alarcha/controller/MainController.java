package com.project.alarcha.controller;

import com.project.alarcha.entities.Counter;
import com.project.alarcha.repositories.CounterRepository;
import com.project.alarcha.service.CounterService;
import com.project.alarcha.service.Population;
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

    @Autowired
    private CounterService counterService;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private Population population;

    @GetMapping("/")
    public void countUser(){
        Counter counter = counterService.getById(1L);

        Integer views = counter.getCount();
        views++;
        counter.setCount(views);

        counterRepository.save(counter);

    }


    @PostMapping("/")
    public void createUser(){
        userService.createSuperAdmin();
    }

    @PostMapping("/populate")
    public void populate(){
        population.create();
    }

}