package com.project.alarcha.controller;

import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import com.project.alarcha.service.ObjectTypeService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/object")
public class ObjectController {
    @Autowired
    private ObjectTypeService objectTypeService;

    @PostMapping("/create")
    public ResponseMessage<ObjectTypeModel> createArea(@RequestBody ObjectTypeModel objectTypeModel){
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.createObjectType(objectTypeModel));
    }
}
