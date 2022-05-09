package com.project.alarcha.controller;

import com.project.alarcha.models.MenuModel.MenuModel;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.service.MenuSectionService;
import com.project.alarcha.service.MenuService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuSectionService menuSectionService;

    @PostMapping("/section/create")
    public ResponseMessage<MenuSectionModel> createMenuSection(@RequestBody MenuSectionModel menuSectionModel){
        return new ResponseMessage<MenuSectionModel>().prepareSuccessMessage(menuSectionService.createMenuSection(menuSectionModel));
    }

    @DeleteMapping("/section/delete/{menuSectionId}")
    public ResponseMessage<MenuSectionModel> deleteMenuSection(@PathVariable Long menuSectionId){
        return new ResponseMessage<MenuSectionModel>().prepareSuccessMessage(menuSectionService.deleteMenuSection(menuSectionId));
    }

    @GetMapping("/section/get-all")
    public ResponseMessage<List<MenuSectionModel>> getMenuSections(){
        return new ResponseMessage<List<MenuSectionModel>>().prepareSuccessMessage(menuSectionService.getAll());
    }

    @GetMapping("/section/get/{menuSectionId}")
    public ResponseMessage<MenuSectionModel> getMenuSection(@PathVariable Long menuSectionId){
        return new ResponseMessage<MenuSectionModel>().prepareSuccessMessage(menuSectionService.getById(menuSectionId));
    }

    @PostMapping("/create")
    public ResponseMessage<MenuModel> createMenu(@RequestBody MenuModel menuModel){
        return new ResponseMessage<MenuModel>().prepareSuccessMessage(menuService.createMenu(menuModel));
    }

    @DeleteMapping("/delete/{menuId}")
    public ResponseMessage<MenuModel> deleteMenu(@PathVariable Long menuId){
        return new ResponseMessage<MenuModel>().prepareSuccessMessage(menuService.deleteMenu(menuId));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<MenuModel>> getMenus(){
        return new ResponseMessage<List<MenuModel>>().prepareSuccessMessage(menuService.getAll());
    }

    @GetMapping("/get/{menuId}")
    public ResponseMessage<MenuModel> getMenu(@PathVariable Long menuId){
        return new ResponseMessage<MenuModel>().prepareSuccessMessage(menuService.getById(menuId));
    }
}