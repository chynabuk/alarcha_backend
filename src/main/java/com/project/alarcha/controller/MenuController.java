package com.project.alarcha.controller;

import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.service.MenuSectionService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuSectionService menuSectionService;

    @PostMapping("/create")
    public ResponseMessage<MenuSectionModel> createArea(@RequestBody MenuSectionModel menuSectionModel){
        return new ResponseMessage<MenuSectionModel>().prepareSuccessMessage(menuSectionService.createMenuSection(menuSectionModel));
    }
}
