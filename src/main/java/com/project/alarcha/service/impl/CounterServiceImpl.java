package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Counter;
import com.project.alarcha.repositories.CounterRepository;
import com.project.alarcha.service.CounterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CounterServiceImpl implements CounterService {

    @Autowired
    CounterRepository counterRepository;


    @Override
    public Counter getById(Long counterId) {
        return counterRepository.getById(counterId);
    }
}
