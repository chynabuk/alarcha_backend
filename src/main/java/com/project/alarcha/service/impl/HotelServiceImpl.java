package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.HotelModel.HotelHallModel;
import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.models.HotelModel.Hotel_ImgModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;
import com.project.alarcha.repositories.AreaRepository;
import com.project.alarcha.repositories.HotelRepository;
import com.project.alarcha.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private HotelHallService hotelHallService;

    @Autowired
    private Hotel_ImgService hotel_imgService;

    @Override
    public HotelModel createHotel(HotelModel hotelModel) {
        Hotel hotel = new Hotel();

        hotelRepository.save(initAndGet(hotel, hotelModel));

        return hotelModel;
    }

    @Override
    public HotelModel getById(Long hotelId) {
        Hotel hotel = getHotel(hotelId);
        return toDetailedModel(hotel);
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
    public List<HotelModel> getForSelect() {
        List<HotelModel> hotelModels = new ArrayList<>();

        for (Hotel hotel : hotelRepository.findAll()){
            if (!hotel.getIsDeleted()){
                HotelModel hotelModel = new HotelModel();
                hotelModel.setId(hotel.getId());
                hotelModel.setHotelName(hotel.getHotelName());
                hotelModels.add(hotelModel);
            }
        }

        return hotelModels;
    }

    @Override
    public List<HotelModel> getForList() {
        List<HotelModel> hotelModels = new ArrayList<>();

        for (Hotel hotel : hotelRepository.findAll()){
            if (!hotel.getIsDeleted()){
                HotelModel hotelModel = new HotelModel();
                hotelModel.setId(hotel.getId());
                hotelModel.setHotelName(hotel.getHotelName());
                hotelModel.setAreaName(hotel.getArea().getAreaName());
                hotelModels.add(toModel(hotel));
            }
        }

        return hotelModels;
    }

    @Override
    public HotelModel updateHotel(HotelModel hotelModel) {
        Hotel hotel = getHotel(hotelModel.getId());

        setValuesOnUpdateHotel(hotel, hotelModel);

        hotelRepository.save(hotel);

        return hotelModel;
    }

    @Override
    public HotelModel deleteHotel(Long hotelId) {
        Hotel hotel = getHotel(hotelId);

        for (RoomType roomType : hotel.getRoomTypes()){
            for (Room room : roomType.getRooms()){
                room.setIsDeleted(true);
            }
            roomType.setIsDeleted(true);
        }

        for (HotelHall hotelHall : hotel.getHotelHalls()){
            hotelHall.setIsDeleted(true);
        }

        hotel.setIsDeleted(true);

        hotelRepository.save(hotel);

        return toModel(hotel);
    }

    @Override
    public List<HotelModel> convertToModel(List<Hotel> hotels) {
        List<HotelModel> hotelModels = new ArrayList<>();

        for (Hotel hotel : hotels){
            if (!hotel.getIsDeleted()){
                hotelModels.add(toModel(hotel));
            }
        }
        return hotelModels;
    }

    private Hotel getHotel(Long id){
        Hotel hotel = hotelRepository.getById(id);

        if (hotel == null){
            throw new ApiFailException("Hotel is not found");
        }
        if (hotel.getIsDeleted()){
            throw new ApiFailException("Hotel is not found or deleted");
        }

        return hotel;
    }

    private Hotel initAndGet(Hotel hotel, HotelModel hotelModel){
        hotel.setHotelName(hotelModel.getHotelName());
        hotel.setArea(areaRepository.getById(hotelModel.getAreaId()));

//        List<RoomTypeModel> roomTypeModels = hotelModel.getRoomTypeModels();
//        if (roomTypeModels != null){
//            roomTypeModels.forEach(roomTypeModel -> roomTypeModel.setHotelName(hotelModel.getHotelName()));
//            List<RoomType> roomTypes = roomTypeService.createRoomTypes(roomTypeModels);
//
//            if (roomTypes != null){
//                hotel.setRoomTypes(roomTypes);
//                roomTypes.forEach(roomType -> roomType.setHotel(hotel));
//            }
//
//        }
//
//        List<HotelHallModel> hotelHallModels = hotelModel.getHotelHallModels();
//        if (hotelHallModels != null){
//            List<HotelHall> hotelHalls = hotelHallService.createHotelHalls(hotelModel.getHotelHallModels());
//            if (hotelHalls != null){
//                hotel.setHotelHalls(hotelHalls);
//                hotelHalls.forEach(hotelHall -> hotelHall.setHotel(hotel));
//            }
//        }

        List<Hotel_ImgModel> hotelImgModels = hotelModel.getHotelImgModels();
        if (hotelImgModels.isEmpty() || hotelImgModels != null){
            List<Hotel_Img> hotelImgs = hotel_imgService.uploadImages(hotelImgModels);
            hotel.setHotelImgs(hotelImgs);
            hotelImgs.forEach(hotel_img -> hotel_img.setHotel(hotel));
        }

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
        hotelModel.setAreaName(hotel.getArea().getAreaName());
        if (hotel.getHotelImgs() != null){
            hotelModel.setImgUrl(new String(hotel.getHotelImgs().get(0).getImg(), StandardCharsets.UTF_8));
        }

        return hotelModel;
    }

    private HotelModel toDetailedModel(Hotel hotel){
        HotelModel hotelModel = new HotelModel();
        hotelModel.setId(hotel.getId());
        hotelModel.setHotelName(hotel.getHotelName());
        hotelModel.setAreaName(hotel.getArea().getAreaName());
        if (hotel.getRoomTypes() != null){
            hotelModel.setRoomTypeModels(roomTypeService.convertToRoomTypeModels(hotel.getRoomTypes()));
        }
        if (hotel.getHotelHalls() != null){
            hotelModel.setHotelHallModels(hotelHallService.convertToModels(hotel.getHotelHalls()));
        }
        if (!hotel.getHotelImgs().isEmpty()){
            if (hotel.getHotelImgs().size() >= 2){
                hotelModel.setImgUrl(new String(hotel.getHotelImgs().get(1).getImg(), StandardCharsets.UTF_8));
            }
        }

        return hotelModel;
    }
}