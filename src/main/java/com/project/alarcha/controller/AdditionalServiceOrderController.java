package com.project.alarcha.controller;

import com.project.alarcha.models.AdditionalServiceModel.AdditionalServiceOrderModel;
import com.project.alarcha.service.AdditionalServiceOrderService;
import com.project.alarcha.service.AdditionalServiceService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/additionalServiceOrder")
public class AdditionalServiceOrderController {
    @Autowired
    private AdditionalServiceService additionalServiceService;

    @Autowired
    private AdditionalServiceOrderService additionalServiceOrderService;

    @PostMapping("/order")
    public ResponseMessage<AdditionalServiceOrderModel> orderAdditionalService(@RequestBody AdditionalServiceOrderModel additionalServiceOrderModel){
        return new ResponseMessage<AdditionalServiceOrderModel>().prepareSuccessMessage(additionalServiceOrderService.order(additionalServiceOrderModel));
    }

    @PostMapping("/order/{additionalServiceOrderId}/accept")
    public ResponseMessage<AdditionalServiceOrderModel> acceptOrder(@PathVariable Long additionalServiceOrderId){
        return new ResponseMessage<AdditionalServiceOrderModel>().prepareSuccessMessage(additionalServiceOrderService.acceptOrder(additionalServiceOrderId));
    }
}
