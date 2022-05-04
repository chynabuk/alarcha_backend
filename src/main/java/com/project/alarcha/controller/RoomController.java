package com.project.alarcha.controller;

import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.service.RoomOrderService;
import com.project.alarcha.service.RoomService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomOrderService roomOrderService;

    @PostMapping("/create")
    public ResponseMessage<RoomModel> createRoom(@RequestBody RoomModel roomModel){
        return new ResponseMessage<RoomModel>().prepareSuccessMessage(roomService.createRoom(roomModel));
    }

    @PostMapping("/order")
    public ResponseMessage<RoomOrderModel> orderRoom(@RequestBody RoomOrderModel roomOrderModel){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.order(roomOrderModel));
    }

    @PostMapping("/order/{roomOrderId}/accept")
    public ResponseMessage<RoomOrderModel> acceptOrder(@PathVariable Long roomOrderId){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.acceptOrder(roomOrderId));
    }

}
