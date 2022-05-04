package com.project.alarcha.controller;

import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.models.HotelModel.HotelHallOrderModel;
import com.project.alarcha.service.HotelHallOrderService;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotelHall")
public class HotelHallController {
    @Autowired
    private HotelHallService hotelHallService;

    @Autowired
    private HotelHallOrderService hotelHallOrderService;

    @PostMapping("/create")
    public ResponseMessage<HotelHallModel> createRoom(@RequestBody HotelHallModel hotelHallModel){
        return new ResponseMessage<HotelHallModel>().prepareSuccessMessage(hotelHallService.createHotelHall(hotelHallModel));
    }

    @PostMapping("/order")
    public ResponseMessage<HotelHallOrderModel> orderRoom(@RequestBody HotelHallOrderModel hotelHallOrderModel){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.order(hotelHallOrderModel));
    }

    @PostMapping("/order/{hotelHallOrderId}/accept")
    public ResponseMessage<HotelHallOrderModel> acceptOrder(@PathVariable Long hotelHallOrderId){
        return new ResponseMessage<HotelHallOrderModel>().prepareSuccessMessage(hotelHallOrderService.acceptOrder(hotelHallOrderId));
    }
}