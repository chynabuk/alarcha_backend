package com.project.alarcha.service.impl;

import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.service.HotelHallService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelHallServiceImpl implements HotelHallService {
    @Override
    public HotelHallModel createHotelHall(HotelHall hotelHall) {
        return null;
    }

    @Override
    public List<HotelHall> convertToHotelHalls(List<HotelHallModel> hotelHallModels) {
        List<HotelHall> hotelHalls = new ArrayList<>();

        for (HotelHallModel hotelHallModel : hotelHallModels){
            HotelHall hotelHall = new HotelHall();
            hotelHall.setName(hotelHallModel.getName());
            hotelHall.setPrice(hotelHallModel.getPrice());

            hotelHalls.add(hotelHall);
        }
        return hotelHalls;
    }
}
