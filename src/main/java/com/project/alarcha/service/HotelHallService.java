package com.project.alarcha.service;

import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.models.HotelModel.HotelHallModel;

import java.util.List;

public interface HotelHallService {
    HotelHallModel createHotelHall(HotelHall hotelHall);
    List<HotelHall> convertToHotelHalls(List<HotelHallModel> hotelHallModels);
}
