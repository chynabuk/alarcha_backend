package com.project.alarcha.controller;

import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import com.project.alarcha.service.ObjectOrderService;
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
    private ObjectService objectService;

    @Autowired
    private ObjectOrderService objectOrderService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @PostMapping("/create")
    public ResponseMessage<ObjectModel> createObject(@RequestBody ObjectModel objectModel){
        return new ResponseMessage<ObjectModel>().prepareSuccessMessage(objectService.createObject(objectModel));
    }

    @DeleteMapping("/delete/{objectId}")
    public ResponseMessage<ObjectModel> deleteObject(@PathVariable(name = "objectId") Long objectId) {
        return new ResponseMessage<ObjectModel>().prepareSuccessMessage(objectService.deleteObject(objectId));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<ObjectModel>> getObjects(){
        return new ResponseMessage<List<ObjectModel>>().prepareSuccessMessage(objectService.getAll());
    }

    @GetMapping("/get/{objectId}")
    public ResponseMessage<ObjectModel> getObject(@PathVariable(name = "objectId") Long objectId){
        return new ResponseMessage<ObjectModel>().prepareSuccessMessage(objectService.getById(objectId));
    }

    @PostMapping("/order")
    public ResponseMessage<ObjectOrderModel> orderObject(@RequestBody ObjectOrderModel objectOrderModel){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.order(objectOrderModel));
    }

    @PostMapping("/order/{objectOrderId}/accept")
    public ResponseMessage<ObjectOrderModel> acceptOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.acceptOrder(objectOrderId));
    }

    @PostMapping("/order/{objectOrderId}/decline")
    public ResponseMessage<ObjectOrderModel> declineOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.declineOrder(objectOrderId));
    }

    @DeleteMapping("/order/delete/{objectOrderId}")
    public ResponseMessage<ObjectOrderModel> deleteOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.deleteOrder(objectOrderId));
    }

    @GetMapping("/order/get-all")
    public ResponseMessage<List<ObjectOrderModel>> getOrders(){
        return new ResponseMessage<List<ObjectOrderModel>>().prepareSuccessMessage(objectOrderService.getAll());
    }

    @GetMapping("/order/get/{objectOrderId}")
    public ResponseMessage<ObjectOrderModel> getOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.getById(objectOrderId));
    }

    @PostMapping("/type/create")
    public ResponseMessage<ObjectTypeModel> createObjectType(@RequestBody ObjectTypeModel objectTypeModel){
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.createObjectType(objectTypeModel));
    }

    @GetMapping("/type/get-all")
    public ResponseMessage<List<ObjectTypeModel>> getAll(){
        return new ResponseMessage<List<ObjectTypeModel>>().prepareSuccessMessage(objectTypeService.getAll());
    }

    @GetMapping("/type/get-for-select")
    public ResponseMessage<List<ObjectTypeModel>> getForObjectTypeSelect(){
        return new ResponseMessage<List<ObjectTypeModel>>().prepareSuccessMessage(objectTypeService.getForSelect());
    }

    @GetMapping("/type/get/{objectTypeId}")
    public ResponseMessage<ObjectTypeModel> getById(@PathVariable Long objectTypeId){
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.getById(objectTypeId));
    }

    @DeleteMapping("/type/delete/{objectTypeId}")
    public ResponseMessage<ObjectTypeModel> deleteObjectType(@PathVariable(name = "objectTypeId") Long objectTypeId) {
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.deleteObjectType(objectTypeId));
    }

}
