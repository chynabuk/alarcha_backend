package com.project.alarcha.controller;


import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceModel;
import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceOrderModel;
import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.service.AdditionalServiceOrderService;
import com.project.alarcha.service.AdditionalServiceService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/additionalService")
public class AdditionalServiceController {
    @Autowired
    AdditionalServiceService additionalServiceService;

    @Autowired
    private AdditionalServiceOrderService additionalServiceOrderService;

    @PostMapping("/create")
    public ResponseMessage<AdditionalServiceModel> createAdditionalService(@RequestBody AdditionalServiceModel additionalServiceModel){
        return new ResponseMessage<AdditionalServiceModel>().prepareSuccessMessage(additionalServiceService.createAdditionalService(additionalServiceModel));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<AdditionalServiceModel>> getAll(){
        return new ResponseMessage<List<AdditionalServiceModel>>().prepareSuccessMessage(additionalServiceService.getAll());
    }

    @GetMapping("/get/{additionalServiceId}")
    public ResponseMessage<AdditionalServiceModel> getById(@PathVariable Long additionalServiceId){
        return new ResponseMessage<AdditionalServiceModel>().prepareSuccessMessage(additionalServiceService.getById(additionalServiceId));
    }

    @DeleteMapping("/delete/{additionalServiceId}")
    public ResponseMessage<AdditionalServiceModel> deleteAdditionalService(@PathVariable Long additionalServiceId){
        return new ResponseMessage<AdditionalServiceModel>().prepareSuccessMessage(additionalServiceService.deleteAdditionalService(additionalServiceId));
    }

    @PostMapping("/order")
    public ResponseMessage<AdditionalServiceOrderModel> orderAdditionalService(@RequestBody AdditionalServiceOrderModel additionalServiceOrderModel){
        return new ResponseMessage<AdditionalServiceOrderModel>().prepareSuccessMessage(additionalServiceOrderService.order(additionalServiceOrderModel));
    }

    @PostMapping("/order/{additionalServiceOrderId}/accept")
    public ResponseMessage<AdditionalServiceOrderModel> acceptOrder(@PathVariable Long additionalServiceOrderId){
        return new ResponseMessage<AdditionalServiceOrderModel>().prepareSuccessMessage(additionalServiceOrderService.acceptOrder(additionalServiceOrderId));
    }
}