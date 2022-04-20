package com.project.alarcha.controller;

import com.project.alarcha.models.AreaModel.AreaModel;
import com.project.alarcha.service.AreaService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @PostMapping("/create")
    public ResponseMessage<AreaModel> createArea(@RequestBody AreaModel areaCreateModel){
        return new ResponseMessage<AreaModel>().prepareSuccessMessage(areaService.createArea(areaCreateModel));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<AreaModel>> getAll(){
        return new ResponseMessage<List<AreaModel>>().prepareSuccessMessage(areaService.getAll());
    }

    @GetMapping("/get/{areaId}")
    public ResponseMessage<AreaModel> getById(@PathVariable Long areaId){
        return new ResponseMessage<AreaModel>().prepareSuccessMessage(areaService.getById(areaId));
    }

    @DeleteMapping("/delete/{areaId}")
    public ResponseMessage<AreaModel> deleteById(@PathVariable Long areaId){
        return new ResponseMessage<AreaModel>().prepareSuccessMessage(areaService.deleteArea(areaId));
    }
}
