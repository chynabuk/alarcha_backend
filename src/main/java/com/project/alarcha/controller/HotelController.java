package com.project.alarcha.controller;

import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.service.HotelService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping("/create")
    public ResponseMessage<HotelModel> createHotel(@RequestBody HotelModel hotelModel){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.createHotel(hotelModel));
    }

    @PutMapping("/update")
    public ResponseMessage<HotelModel> updateHotel(@RequestBody HotelModel hotelModel){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.updateHotel(hotelModel));
    }

    @GetMapping("/get-all")
    public ResponseMessage<List<HotelModel>> getAll(){
        return new ResponseMessage<List<HotelModel>>().prepareSuccessMessage(hotelService.getAll());
    }

    @GetMapping("/get-for-select")
    public ResponseMessage<List<HotelModel>> getForSelect(){
        return new ResponseMessage<List<HotelModel>>().prepareSuccessMessage(hotelService.getForSelect());
    }

    @GetMapping("/get-for-list")
    public ResponseMessage<List<HotelModel>> getForList(){
        return new ResponseMessage<List<HotelModel>>().prepareSuccessMessage(hotelService.getForList());
    }

    @GetMapping("/get/{hotelId}")
    public ResponseMessage<HotelModel> getById(@PathVariable Long hotelId){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.getById(hotelId));
    }

    @DeleteMapping("/delete/{hotelId}")
    public ResponseMessage<HotelModel> deleteHotel(@PathVariable Long hotelId){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.deleteHotel(hotelId));
    }

}
