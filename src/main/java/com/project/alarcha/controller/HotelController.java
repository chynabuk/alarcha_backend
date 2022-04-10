package com.project.alarcha.controller;

import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.service.HotelService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping("/create")
    public ResponseMessage<HotelModel> createArea(@RequestBody HotelModel hotelModel){
        return new ResponseMessage<HotelModel>().prepareSuccessMessage(hotelService.createHotel(hotelModel));
    }

}
