package com.project.alarcha.service;

import com.project.alarcha.models.HotelModel.HotelModel;

import java.util.List;

public interface HotelService {
    HotelModel createHotel(HotelModel hotelModel);
    HotelModel getById(Long hotelId);
    List<HotelModel> getAll();
    HotelModel updateHotel(HotelModel hotelModel);
    HotelModel deleteHotel(Long hotelId);
}
