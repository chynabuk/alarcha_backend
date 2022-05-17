package com.project.alarcha.controller;

import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.models.RoomModel.RoomOrderModel;
import com.project.alarcha.models.RoomModel.RoomOrderPayModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;
import com.project.alarcha.service.RoomOrderService;
import com.project.alarcha.service.RoomService;
import com.project.alarcha.service.RoomTypeService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomOrderService roomOrderService;

    @Autowired
    private RoomTypeService roomTypeService;

    @PostMapping("/type/create")
    public ResponseMessage<RoomTypeModel> createRoomType(@RequestBody RoomTypeModel roomTypeModel){
        return new ResponseMessage<RoomTypeModel>().prepareSuccessMessage(roomTypeService.createRoomType(roomTypeModel));
    }

    @DeleteMapping("/type/delete/{roomTypeId}")
    public ResponseMessage<RoomTypeModel> deleteRoomType(@PathVariable Long roomTypeId){
        return new ResponseMessage<RoomTypeModel>().prepareSuccessMessage(roomTypeService.deleteRoomType(roomTypeId));
    }

    @GetMapping("/type/get-all")
    public ResponseMessage<List<RoomTypeModel>> getRoomTypes(){
        return new ResponseMessage<List<RoomTypeModel>>().prepareSuccessMessage(roomTypeService.getAll());
    }

    @GetMapping("/type/get-for-select")
    public ResponseMessage<List<RoomTypeModel>> getForRoomTypeSelect(){
        return new ResponseMessage<List<RoomTypeModel>>().prepareSuccessMessage(roomTypeService.getForSelect());
    }

    @GetMapping("/type/get-for-list")
    public ResponseMessage<List<RoomTypeModel>> getForRoomTypeList(){
        return new ResponseMessage<List<RoomTypeModel>>().prepareSuccessMessage(roomTypeService.getForList());
    }

    @GetMapping("/type/get/{roomTypeId}")
    public ResponseMessage<RoomTypeModel> getRoomType(@PathVariable Long roomTypeId){
        return new ResponseMessage<RoomTypeModel>().prepareSuccessMessage(roomTypeService.getById(roomTypeId));
    }

    @PostMapping("/create")
    public ResponseMessage<RoomModel> createRoom(@RequestBody RoomModel roomModel){
        return new ResponseMessage<RoomModel>().prepareSuccessMessage(roomService.createRoom(roomModel));
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseMessage<RoomModel> deleteRoom(@PathVariable Long roomId){
        return new ResponseMessage<RoomModel>().prepareSuccessMessage(roomService.deleteRoom(roomId));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<RoomModel>> getRooms(){
        return new ResponseMessage<List<RoomModel>>().prepareSuccessMessage(roomService.getAll());
    }

    @GetMapping("/get-for-list")
    public ResponseMessage<List<RoomModel>> getRoomsForList(){
        return new ResponseMessage<List<RoomModel>>().prepareSuccessMessage(roomService.getForList());
    }

    @GetMapping("/get/{roomId}")
    public ResponseMessage<RoomModel> getRoom(@PathVariable Long roomId){
        return new ResponseMessage<RoomModel>().prepareSuccessMessage(roomService.getById(roomId));
    }

    @PostMapping("/order")
    public ResponseMessage<RoomOrderModel> orderRoom(@RequestBody RoomOrderModel roomOrderModel){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.order(roomOrderModel));
    }

    @PostMapping("/order/pay")
    public ResponseMessage<RoomOrderPayModel> payOrderRoom(@RequestBody RoomOrderPayModel roomOrderPayModel){
        return new ResponseMessage<RoomOrderPayModel>().prepareSuccessMessage(roomOrderService.pay(roomOrderPayModel));
    }

    @PostMapping("/order/{roomOrderId}/accept")
    public ResponseMessage<RoomOrderModel> acceptOrder(@PathVariable Long roomOrderId){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.acceptOrder(roomOrderId));
    }

    @PostMapping("/order/{roomOrderId}/decline")
    public ResponseMessage<RoomOrderModel> declineOrder(@PathVariable Long roomOrderId){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.declineOrder(roomOrderId));
    }

    @PostMapping("/order/{roomOrderId}/accept-pay")
    public ResponseMessage<RoomOrderModel> acceptPay(@PathVariable Long roomOrderId){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.acceptOrder(roomOrderId));
    }

    @DeleteMapping("/order/delete/{roomOrderId}")
    public ResponseMessage<RoomOrderModel> deleteOrder(@PathVariable Long roomOrderId){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.deleteOrder(roomOrderId));
    }

    @GetMapping("/order/get-all")
    public ResponseMessage<List<RoomOrderModel>> getOrders(){
        return new ResponseMessage<List<RoomOrderModel>>().prepareSuccessMessage(roomOrderService.getAll());
    }

    @GetMapping("/order/get-confirmed-or-declined")
    public ResponseMessage<List<RoomOrderModel>> getConfirmedOrDeclinedOrders(){
        return new ResponseMessage<List<RoomOrderModel>>().prepareSuccessMessage(roomOrderService.getConfirmedOrDeclinedOrders());
    }

    @GetMapping("/order/get-in-process")
    public ResponseMessage<List<RoomOrderModel>> getInProcessOrders(){
        return new ResponseMessage<List<RoomOrderModel>>().prepareSuccessMessage(roomOrderService.getInProcessOrders());
    }

    @GetMapping("/order/get/{roomOrderId}")
    public ResponseMessage<RoomOrderModel> getOrder(@PathVariable Long roomOrderId){
        return new ResponseMessage<RoomOrderModel>().prepareSuccessMessage(roomOrderService.getById(roomOrderId));
    }

}
