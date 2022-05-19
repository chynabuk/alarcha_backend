package com.project.alarcha.controller;

import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
import com.project.alarcha.models.HotelModel.HotelHallOrderPayModel;
import com.project.alarcha.service.HotelHallOrderService;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotelHall")
public class HotelHallController {
    @Autowired
    private HotelHallService hotelHallService;

    @Autowired
    private HotelHallOrderService hotelHallOrderService;

    @PostMapping("/create")
    public ResponseMessage<HotelHallModel> createHotelHall(@RequestBody HotelHallModel hotelHallModel){
        return new ResponseMessage<HotelHallModel>().prepareSuccessMessage(hotelHallService.createHotelHall(hotelHallModel));
    }

    @DeleteMapping("/delete/{hotelHallId}")
    public ResponseMessage<HotelHallModel> deleteHotelHall(@PathVariable Long hotelHallId){
        return new ResponseMessage<HotelHallModel>().prepareSuccessMessage(hotelHallService.deleteHotelHall(hotelHallId));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<HotelHallModel>> getHotelHalls(){
        return new ResponseMessage<List<HotelHallModel>>().prepareSuccessMessage(hotelHallService.getAll());
    }

    @GetMapping("/get-for-list")
    public ResponseMessage<List<HotelHallModel>> getHotelHallsForList(){
        return new ResponseMessage<List<HotelHallModel>>().prepareSuccessMessage(hotelHallService.getForList());
    }

    @GetMapping("/get/{hotelHallId}")
    public ResponseMessage<HotelHallModel> getHotelHall(@PathVariable Long hotelHallId){
        return new ResponseMessage<HotelHallModel>().prepareSuccessMessage(hotelHallService.getById(hotelHallId));
    }

    @PostMapping("/order")
    public ResponseMessage<HotelHallOrderModel> orderHotelHall(@RequestBody HotelHallOrderModel hotelHallOrderModel){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.order(hotelHallOrderModel));
    }

    @PostMapping("/order/pay")
    public ResponseMessage<HotelHallOrderPayModel> orderHotelHall(@RequestBody HotelHallOrderPayModel hotelHallOrderPayModel){
        return new ResponseMessage<HotelHallOrderPayModel>().prepareSuccessMessage(hotelHallOrderService.pay(hotelHallOrderPayModel));
    }

    @PostMapping("/order/{hotelHallOrderId}/accept")
    public ResponseMessage<HotelHallOrderModel> acceptOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.acceptOrder(hotelHallOrderId));
    }

    @PostMapping("/order/{hotelHallOrderId}/decline")
    public ResponseMessage<HotelHallOrderModel> declineOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.declineOrder(hotelHallOrderId));
    }

    @PostMapping("/order/{hotelHallOrderId}/accept-pay")
    public ResponseMessage<HotelHallOrderModel> acceptPay(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.acceptPayOrder(hotelHallOrderId));
    }

    @DeleteMapping("/order/delete/{hotelHallOrderId}")
    public ResponseMessage<HotelHallOrderModel> deleteOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.deleteOrder(hotelHallOrderId));
    }

    @GetMapping("/order/get-all")
    public ResponseMessage<List<HotelHallOrderModel>> getOrders(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<HotelHallOrderModel>>().prepareSuccessMessage(hotelHallOrderService.getAll(page));
    }

    @GetMapping("/order/get-in-process")
    public ResponseMessage<List<HotelHallOrderModel>> getInProcessOrders(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<HotelHallOrderModel>>().prepareSuccessMessage(hotelHallOrderService.getInProcessOrders(page));
    }

    @GetMapping("/order/get-confirmed-or-declined")
    public ResponseMessage<List<HotelHallOrderModel>> getConfirmedOrDeclinedOrders(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<HotelHallOrderModel>>().prepareSuccessMessage(hotelHallOrderService.getConfirmedOrDeclinedOrders(page));
    }

    @GetMapping("/order/get-in-pay-check")
    public ResponseMessage<List<HotelHallOrderModel>> getInPayCheck(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<HotelHallOrderModel>>().prepareSuccessMessage(hotelHallOrderService.getInCheckPay(page));
    }

    @GetMapping("/order/get-paid")
    public ResponseMessage<List<HotelHallOrderModel>> getPaid(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return new ResponseMessage<List<HotelHallOrderModel>>().prepareSuccessMessage(hotelHallOrderService.getCheckedPay(page));
    }

    @GetMapping("/order/get/{hotelHallOrderId}")
    public ResponseMessage<HotelHallOrderModel> getOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.getById(hotelHallOrderId));
    }
}