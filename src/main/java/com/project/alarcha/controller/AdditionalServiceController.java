package com.project.alarcha.controller;


import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceModel;
import com.project.alarcha.service.AdditionalServiceService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/additionalService")
public class AdditionalServiceController {
    @Autowired
    AdditionalServiceService additionalServiceService;

    @PostMapping("/create")
    public ResponseMessage<AdditionalServiceModel> createAdditionalService(@RequestBody AdditionalServiceModel additionalServiceModel){
        return new ResponseMessage<AdditionalServiceModel>().prepareSuccessMessage(additionalServiceService.createAdditionalService(additionalServiceModel));
    }
}