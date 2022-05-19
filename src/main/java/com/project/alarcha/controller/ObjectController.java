package com.project.alarcha.controller;

import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderModel;
import com.project.alarcha.models.ObjectModel.ObjectOrderPayModel;
import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import com.project.alarcha.service.ObjectOrderService;
import com.project.alarcha.service.ObjectService;
import com.project.alarcha.service.ObjectTypeService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/get-for-list")
    public ResponseMessage<List<ObjectModel>> getObjectsForList(){
        return new ResponseMessage<List<ObjectModel>>().prepareSuccessMessage(objectService.getForList());
    }

    @GetMapping("/get/{objectId}")
    public ResponseMessage<ObjectModel> getObject(@PathVariable(name = "objectId") Long objectId){
        return new ResponseMessage<ObjectModel>().prepareSuccessMessage(objectService.getById(objectId));
    }

    @PutMapping("/update")
    public ResponseMessage<ObjectModel> updateObject(@Valid @RequestBody ObjectModel objectModel) {
        return new ResponseMessage<ObjectModel>().prepareSuccessMessage(objectService.updateObject(objectModel));
    }

    @PostMapping("/order")
    public ResponseMessage<ObjectOrderModel> orderObject(@RequestBody ObjectOrderModel objectOrderModel){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.order(objectOrderModel));
    }

    @PostMapping("/order/pay")
    public ResponseMessage<ObjectOrderPayModel> orderObject(@RequestBody ObjectOrderPayModel objectOrderPayModel){
        return new ResponseMessage<ObjectOrderPayModel>().prepareSuccessMessage(objectOrderService.pay(objectOrderPayModel));
    }

    @PostMapping("/order/{objectOrderId}/accept")
    public ResponseMessage<ObjectOrderModel> acceptOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.acceptOrder(objectOrderId));
    }

    @PostMapping("/order/{objectOrderId}/decline")
    public ResponseMessage<ObjectOrderModel> declineOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.declineOrder(objectOrderId));
    }

    @PostMapping("/order/{objectOrderId}/accept-pay")
    public ResponseMessage<ObjectOrderModel> acceptPay(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.acceptPayOrder(objectOrderId));
    }

    @DeleteMapping("/order/delete/{objectOrderId}")
    public ResponseMessage<ObjectOrderModel> deleteOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.deleteOrder(objectOrderId));
    }

    @GetMapping("/order/get-all")
    public ResponseMessage<List<ObjectOrderModel>> getOrders(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<ObjectOrderModel>>().prepareSuccessMessage(objectOrderService.getAll(page));
    }

    @GetMapping("/order/get-in-process")
    public ResponseMessage<List<ObjectOrderModel>> getInProcessOrders(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<ObjectOrderModel>>().prepareSuccessMessage(objectOrderService.getInProcessOrders(page));
    }

    @GetMapping("/order/get-in-pay-check")
    public ResponseMessage<List<ObjectOrderModel>> getInPayCheck(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<ObjectOrderModel>>().prepareSuccessMessage(objectOrderService.getInCheckPay(page));
    }

    @GetMapping("/order/get-paid")
    public ResponseMessage<List<ObjectOrderModel>> getPaid(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<ObjectOrderModel>>().prepareSuccessMessage(objectOrderService.getCheckedPay(page));
    }

    @GetMapping("/order/get-confirmed-or-declined")
    public ResponseMessage<List<ObjectOrderModel>> getConfirmedOrDeclinedOrders(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<ObjectOrderModel>>().prepareSuccessMessage(objectOrderService.getConfirmedOrDeclinedOrders(page));
    }

    @GetMapping("/order/get/{objectOrderId}")
    public ResponseMessage<ObjectOrderModel> getOrder(@PathVariable Long objectOrderId){
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.getById(objectOrderId));
    }

    @PutMapping("/order/update")
    public ResponseMessage<ObjectOrderModel> updateOrder(@Valid @RequestBody ObjectOrderModel objectOrderModel) {
        return new ResponseMessage<ObjectOrderModel>().prepareSuccessMessage(objectOrderService.updateOrder(objectOrderModel));
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

    @GetMapping("/type/get-for-list")
    public ResponseMessage<List<ObjectTypeModel>> getForObjectTypeForList(){
        return new ResponseMessage<List<ObjectTypeModel>>().prepareSuccessMessage(objectTypeService.getForList());
    }

    @GetMapping("/type/get/{objectTypeId}")
    public ResponseMessage<ObjectTypeModel> getById(@PathVariable Long objectTypeId){
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.getById(objectTypeId));
    }

    @DeleteMapping("/type/delete/{objectTypeId}")
    public ResponseMessage<ObjectTypeModel> deleteObjectType(@PathVariable(name = "objectTypeId") Long objectTypeId) {
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.deleteObjectType(objectTypeId));
    }

    @PutMapping("/type/update")
    public ResponseMessage<ObjectTypeModel> updateObjectType(@Valid @RequestBody ObjectTypeModel objectTypeModel) {
        return new ResponseMessage<ObjectTypeModel>().prepareSuccessMessage(objectTypeService.updateObjectType(objectTypeModel));
    }

}
