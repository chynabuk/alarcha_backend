package com.project.alarcha.controller;


import com.project.alarcha.models.BlogModel.BlogModel;
import com.project.alarcha.models.NatureModel.NatureModel;
import com.project.alarcha.service.BlogService;
import com.project.alarcha.service.NatureService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nature")
public class NatureController {
    @Autowired
    NatureService natureService;

    @PostMapping("/create")
    public ResponseMessage<NatureModel> createNature(@RequestBody NatureModel natureModel){
        return new ResponseMessage<NatureModel>().prepareSuccessMessage(natureService.createNature(natureModel));
    }
}
