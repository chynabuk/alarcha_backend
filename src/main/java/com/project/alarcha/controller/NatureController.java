package com.project.alarcha.controller;


import com.project.alarcha.models.NatureModel.NatureModel;
import com.project.alarcha.models.NatureModel.NatureTypeModel;
import com.project.alarcha.service.NatureService;
import com.project.alarcha.service.NatureTypeService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nature")
public class NatureController {
    @Autowired
    private NatureService natureService;

    @Autowired
    private NatureTypeService natureTypeService;

    @PostMapping("/type/create")
    public ResponseMessage<NatureTypeModel> createNatureType(@RequestBody NatureTypeModel natureTypeModel){
        return new ResponseMessage<NatureTypeModel>().prepareSuccessMessage(natureTypeService.createNatureType(natureTypeModel));
    }

    @PutMapping("/type/update")
    public ResponseMessage<NatureTypeModel> updateNatureType(@RequestBody NatureTypeModel natureTypeModel){
        return new ResponseMessage<NatureTypeModel>().prepareSuccessMessage(natureTypeService.updateNatureType(natureTypeModel));
    }

    @PostMapping("/type/create-flora-and-fauna")
    public ResponseMessage<List<NatureTypeModel>> createFloraAndFauna(){
        return new ResponseMessage<List<NatureTypeModel>>().prepareSuccessMessage(natureTypeService.createFloraAndFauna());
    }

    @DeleteMapping("/type/delete/{id}")
    public ResponseMessage<NatureTypeModel> deleteNatureType(@PathVariable Long id){
        return new ResponseMessage<NatureTypeModel>().prepareSuccessMessage(natureTypeService.deleteNatureType(id));
    }

    @GetMapping("/type/get-all")
    public ResponseMessage<List<NatureTypeModel>> getNatureTypes(){
        return new ResponseMessage<List<NatureTypeModel>>().prepareSuccessMessage(natureTypeService.getAll());
    }

    @GetMapping("/type/get-for-select")
    public ResponseMessage<List<NatureTypeModel>> getNatureTypesForSelect(){
        return new ResponseMessage<List<NatureTypeModel>>().prepareSuccessMessage(natureTypeService.getForSelect());
    }


    @GetMapping("/type/get/{id}")
    public ResponseMessage<NatureTypeModel> getNatureType(@PathVariable Long id){
        return new ResponseMessage<NatureTypeModel>().prepareSuccessMessage(natureTypeService.getById(id));
    }

    @PostMapping("/create")
    public ResponseMessage<NatureModel> createNature(@RequestBody NatureModel natureModel){
        return new ResponseMessage<NatureModel>().prepareSuccessMessage(natureService.createNature(natureModel));
    }

    @PutMapping("/update")
    public ResponseMessage<NatureModel> updateNature(@RequestBody NatureModel natureModel){
        return new ResponseMessage<NatureModel>().prepareSuccessMessage(natureService.updateNature(natureModel));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage<NatureModel> deleteNature(@PathVariable Long id){
        return new ResponseMessage<NatureModel>().prepareSuccessMessage(natureService.deleteNature(id));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<NatureModel>> getNatures(){
        return new ResponseMessage<List<NatureModel>>().prepareSuccessMessage(natureService.getAll());
    }

    @GetMapping("/get-for-list")
    public ResponseMessage<List<NatureModel>> getNaturesForList(){
        return new ResponseMessage<List<NatureModel>>().prepareSuccessMessage(natureService.getForList());
    }

    @GetMapping("/get/{id}")
    public ResponseMessage<NatureModel> getNature(@PathVariable Long id){
        return new ResponseMessage<NatureModel>().prepareSuccessMessage(natureService.getById(id));
    }
}
