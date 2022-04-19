package com.project.alarcha.controller;

import com.project.alarcha.models.AreaModel.AreaModel;
import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.service.HotelService;
import com.project.alarcha.service.RoomService;
import com.project.alarcha.service.RoomTypeService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelHallService hotelHallService;

    @PostMapping("/create")
    public ResponseMessage<HotelModel> createArea(@RequestBody HotelModel hotelModel){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.createHotel(hotelModel));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<HotelModel>> getAll(){
        return new ResponseMessage<List<HotelModel>>().prepareSuccessMessage(hotelService.getAll());
    }

    @GetMapping("/get/{hotelId}")
    public ResponseMessage<HotelModel> getById(@PathVariable Long hotelId){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.getById(hotelId));
    }

    @DeleteMapping("/delete/{hotelId}")
    public ResponseMessage<HotelModel> deleteArea(@PathVariable Long hotelId){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.deleteHotel(hotelId));
    }

}
