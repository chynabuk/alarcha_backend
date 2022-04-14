package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Hotel;
import com.project.alarcha.entities.HotelHall;
import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;
import com.project.alarcha.repositories.HotelRepository;
import com.project.alarcha.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private HotelHallService hotelHallService;

    @Override
    public HotelModel createHotel(HotelModel hotelModel) {
        Hotel hotel = new Hotel();

        hotelRepository.save(initAndGet(hotel, hotelModel));

        return hotelModel;
    }

    @Override
    public HotelModel getById(Long hotelId) {
        Hotel hotel = hotelRepository.getById(hotelId);
        return toModel(hotel);
    }

    @Override
    public List<HotelModel> getAll() {
        List<HotelModel> hotelModels = new ArrayList<>();

        for (Hotel hotel : hotelRepository.findAll()){
            if (!hotel.getIsDeleted()){
                hotelModels.add(toModel(hotel));
            }
        }

        return hotelModels;
    }

    @Override
    public HotelModel updateHotel(HotelModel hotelModel) {
        Hotel hotel = hotelRepository.getById(hotelModel.getId());

        setValuesOnUpdateHotel(hotel, hotelModel);

        hotelRepository.save(hotel);

        return hotelModel;
    }

    @Override
    public HotelModel deleteHotel(Long hotelId) {
        Hotel hotel = hotelRepository.getById(hotelId);

        if (hotel != null){
            if (hotel.getIsDeleted()){
                throw new ApiFailException("Hotel is already deleted");
            }
            hotel.setIsDeleted(true);
        }

        hotelRepository.save(hotel);

        return toModel(hotel);
    }

    private Hotel initAndGet(Hotel hotel, HotelModel hotelModel){
        hotel.setHotelName(hotelModel.getHotelName());
        hotel.setArea(areaService.getById(hotelModel.getAreaId()));

        List<RoomTypeModel> roomTypeModels = hotelModel.getRoomTypeModels();
        roomTypeModels.forEach(roomTypeModel -> roomTypeModel.setHotelName(hotelModel.getHotelName()));

        System.out.println(hotelModel.getHotelName());

        List<RoomType> roomTypes = roomTypeService.createRoomTypes(roomTypeModels);
        hotel.setRoomTypes(roomTypes);
        roomTypes.forEach(roomType -> roomType.setHotel(hotel));

        List<HotelHall> hotelHalls = hotelHallService.createHotelHalls(hotelModel.getHotelHallModels());
        hotel.setHotelHalls(hotelHalls);
        hotelHalls.forEach(hotelHall -> hotelHall.setHotel(hotel));

        hotel.setIsDeleted(false);

        return hotel;
    }

    private void setValuesOnUpdateHotel(Hotel hotel, HotelModel hotelModel){
        String hotelName = hotelModel.getHotelName();
        List<RoomTypeModel> roomTypeModels = hotelModel.getRoomTypeModels();
        List<HotelHallModel> hotelHallModels = hotelModel.getHotelHallModels();

        if (hotelName != null){
            hotel.setHotelName(hotelName);
        }
        if (roomTypeModels != null){
            List<RoomType> roomTypes = roomTypeService.createRoomTypes(roomTypeModels);
            hotel.setRoomTypes(roomTypes);
            roomTypes.forEach(roomType -> roomType.setHotel(hotel));
        }
        if (hotelHallModels != null){
            List<HotelHall> hotelHalls = hotelHallService.createHotelHalls(hotelHallModels);
            hotel.setHotelHalls(hotelHalls);
            hotelHalls.forEach(hotelHall -> hotelHall.setHotel(hotel));
        }
    }

    private HotelModel toModel(Hotel hotel){
        HotelModel hotelModel = new HotelModel();
        hotelModel.setId(hotel.getId());
        hotelModel.setHotelName(hotel.getHotelName());
        hotelModel.setRoomTypeModels(roomTypeService.getAll());
        hotelModel.setHotelHallModels(hotelHallService.getAll());
        hotelModel.setAreaName(hotel.getArea().getAreaName());

        return hotelModel;
    }
}