package com.project.alarcha.controller;

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
@RequestMapping("/object")
public class ObjectController {
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
}
