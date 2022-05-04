package com.project.alarcha.controller;

import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.service.ObjectOrderService;
import com.project.alarcha.service.ObjectService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/object")
public class ObjectController {
    @Autowired
    private ObjectService objectService;

    @Autowired
    private ObjectOrderService objectOrderService;

    @PostMapping("/order")
    public ResponseMessage<ObjectOrderModel> orderObject(@RequestBody ObjectOrderModel objectOrderModel){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.order(objectOrderModel));
    }

    @PostMapping("/order/{objectOrderId}/accept")
    public ResponseMessage<ObjectOrderModel> acceptOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.acceptOrder(objectOrderId));
    }

}
