package com.project.alarcha.controller;

import com.project.alarcha.models.MenuModel.MenuModel;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import com.project.alarcha.service.MenuSectionService;
import com.project.alarcha.service.MenuService;
import com.project.alarcha.service.ObjectService;
import com.project.alarcha.service.ObjectTypeService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/objectType")
public class ObjectTypeController {
    @Autowired
    private ObjectTypeService objectTypeService;

    @Autowired
    private ObjectService objectService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuSectionService menuSectionService;

    @PostMapping("/create")
    public ResponseMessage<ObjectTypeModel> createObjectType(@RequestBody ObjectTypeModel objectTypeModel){
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.createObjectType(objectTypeModel));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<ObjectTypeModel>> getAll(){
        return new ResponseMessage<List<ObjectTypeModel>>().prepareSuccessMessage(objectTypeService.getAll());
    }

    @GetMapping("/get/{objectTypeId}")
    public ResponseMessage<ObjectTypeModel> getById(@PathVariable Long objectTypeId){
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.getById(objectTypeId));
    }

    @DeleteMapping("/delete/{objectTypeId}")
    public ResponseMessage<ObjectTypeModel> deleteObjectType(@PathVariable(name = "objectTypeId") Long objectTypeId) {
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.deleteObjectType(objectTypeId));
    }

    @DeleteMapping("/menu/delete/{menuId}")
    public ResponseMessage<MenuModel> deleteMenu(@PathVariable(name = "menuId") Long menuId) {
        return new ResponseMessage<MenuModel>().prepareSuccessMessage(menuService.deleteMenu(menuId));
    }

    @DeleteMapping("/object/delete/{objectId}")
    public ResponseMessage<ObjectModel> deleteObject(@PathVariable(name = "objectId") Long objectId) {
        return new ResponseMessage<ObjectModel>().prepareSuccessMessage(objectService.deleteObject(objectId));
    }

    @DeleteMapping("/menuSection/delete/{menuSectionId}")
    public ResponseMessage<MenuSectionModel> deleteMenuSection(@PathVariable(name = "menuSectionId") Long menuSectionId) {
        return new ResponseMessage<MenuSectionModel>().prepareSuccessMessage(menuSectionService.deleteMenuSection(menuSectionId));
    }

}
