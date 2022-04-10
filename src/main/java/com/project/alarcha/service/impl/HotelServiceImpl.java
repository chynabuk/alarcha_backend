package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Hotel;
import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.entities.Room;
import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.repositories.HotelRepository;
import com.project.alarcha.service.AreaService;
import com.project.alarcha.service.HotelHallService;
import com.project.alarcha.service.HotelService;
import com.project.alarcha.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelHallService hotelHallService;

    @Override
    public HotelModel createHotel(HotelModel hotelModel) {
        Hotel hotel = new Hotel();

        hotelRepository.save(initAndGet(hotel, hotelModel));

        return hotelModel;
    }

    private Hotel initAndGet(Hotel hotel, HotelModel hotelModel){
        hotel.setHotelName(hotelModel.getHotelName());
        hotel.setArea(areaService.getById(hotelModel.getAreaId()));
        hotel.setRooms(
                roomService.convertToRoom(hotelModel.getRoomModels())
        );

        for (Room room : hotel.getRooms()){
            room.setHotel(hotel);
        }

        hotel.setHotelHalls(
                hotelHallService.convertToHotelHalls(hotelModel.getHotelHallModels())
        );

        for (HotelHall hotelHall : hotel.getHotelHalls()){
            hotelHall.setHotel(hotel);
        }

        return hotel;
    }
}
