package com.project.alarcha.service;

import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.models.HotelModel.HotelHallModel;

import java.util.List;

public interface HotelHallService {
    HotelHallModel createHotelHall(HotelHallModel hotelHallModel);
    List<HotelHall> createHotelHalls(List<HotelHallModel> hotelHallModelModels);
    List<HotelHall> convertToHotelHalls(List<HotelHallModel> hotelHallModelModels);
    HotelHallModel getById(Long id);
    HotelHall getHotelHallById(long id);
    List<HotelHallModel> getAll();
    HotelHallModel updateHotelHall(HotelHallModel hotelHallModel);
    HotelHallModel deleteHotelHall(Long id);
}
