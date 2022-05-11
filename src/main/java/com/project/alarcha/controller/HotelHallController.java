package com.project.alarcha.controller;

import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
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

    @PostMapping("/order/{hotelHallOrderId}/accept")
    public ResponseMessage<HotelHallOrderModel> acceptOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.acceptOrder(hotelHallOrderId));
    }

    @PostMapping("/order/{hotelHallOrderId}/decline")
    public ResponseMessage<HotelHallOrderModel> declineOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.declineOrder(hotelHallOrderId));
    }

    @DeleteMapping("/order/delete/{hotelHallOrderId}")
    public ResponseMessage<HotelHallOrderModel> deleteOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.deleteOrder(hotelHallOrderId));
    }

    @PostMapping("/order/get-all")
    public ResponseMessage<List<HotelHallOrderModel>> getOrders(){
        return new ResponseMessage<List<HotelHallOrderModel>>().prepareSuccessMessage(hotelHallOrderService.getAll());
    }

    @PostMapping("/order/get/{hotelHallOrderId}")
    public ResponseMessage<HotelHallOrderModel> getOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.getById(hotelHallOrderId));
    }
}