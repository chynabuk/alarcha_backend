package com.project.alarcha.controller;

import com.project.alarcha.models.AreaModel.AreaModel;
import com.project.alarcha.service.AreaService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @PostMapping("/create")
    public ResponseMessage<AreaModel> createArea(@RequestBody AreaModel areaCreateModel){
        return new ResponseMessage<AreaModel>().prepareSuccessMessage(areaService.createArea(areaCreateModel));
    }
}
