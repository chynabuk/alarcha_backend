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

        for (Hotel hotel : hotelRepository.getAll()){
            hotelModels.add(toModel(hotel));
        }

        return hotelModels;
    }

    @Override
    public List<HotelModel> getForSelect() {
        List<HotelModel> hotelModels = new ArrayList<>();

        for (Hotel hotel : hotelRepository.getAll()){
            HotelModel hotelModel = new HotelModel();
            hotelModel.setId(hotel.getId());
            hotelModel.setHotelName(hotel.getHotelName());
            hotelModels.add(hotelModel);
        }

        return hotelModels;
    }

    @Override
    public List<HotelModel> getForList() {
        List<HotelModel> hotelModels = new ArrayList<>();

        for (Hotel hotel : hotelRepository.getAll()){
            HotelModel hotelModel = new HotelModel();
            hotelModel.setId(hotel.getId());
            hotelModel.setHotelName(hotel.getHotelName());
            hotelModel.setAreaName(hotel.getArea().getAreaName());
            hotelModels.add(toModel(hotel));
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
            roomType.setIsDeleted(true);
            for (Room room : roomType.getRooms()){
                room.setIsDeleted(true);
            }
        }

        for (HotelHall hotelHall : hotel.getHotelHalls()){
            hotelHall.setIsDeleted(true);

            for (HotelHallOrder hotelHallOrder : hotelHall.getHotelHallOrders()){
                if (!hotelHallOrder.getIsDeleted()){
                    hotelHallOrder.setIsDeleted(true);
                }
            }
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
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ApiFailException("Hotel не найден."));

        if (hotel.getIsDeleted()){
            throw new ApiFailException("Hotel не найден или удален.");
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
//        List<RoomTypeModel> roomTypeModels = hotelModel.getRoomTypeModels();
//        List<HotelHallModel> hotelHallModels = hotelModel.getHotelHallModels();

        if (hotelName != null){
            hotel.setHotelName(hotelName);
        }
//        if (roomTypeModels != null){
//            List<RoomType> roomTypes = roomTypeService.createRoomTypes(roomTypeModels);
//            hotel.setRoomTypes(roomTypes);
//            roomTypes.forEach(roomType -> roomType.setHotel(hotel));
//        }
//        if (hotelHallModels != null){
//            List<HotelHall> hotelHalls = hotelHallService.createHotelHalls(hotelHallModels);
//            hotel.setHotelHalls(hotelHalls);
//            hotelHalls.forEach(hotelHall -> hotelHall.setHotel(hotel));
//        }

    }

    private Float getMinPrice(Hotel hotel){
        List<RoomType> roomTypes = hotel.getRoomTypes();

        float minPrice = 0;

        if (!roomTypes.isEmpty()){
            RoomType roomType = roomTypes.stream().min((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())).get();
            minPrice = roomType.getPrice();
        }

        return minPrice;
    }

    private HotelModel toModel(Hotel hotel){
        HotelModel hotelModel = new HotelModel();
        hotelModel.setId(hotel.getId());
        hotelModel.setHotelName(hotel.getHotelName());
        hotelModel.setAreaName(hotel.getArea().getAreaName());

        List<Hotel_Img> hotelImgs = hotel.getHotelImgs();
        if (hotelImgs != null){
            if (!hotelImgs.isEmpty()){
                hotelModel.setImgUrl(new String(hotelImgs.get(0).getImg(), StandardCharsets.UTF_8));
            }
        }

        hotelModel.setMinPrice(getMinPrice(hotel));
        return hotelModel;
    }

    private HotelModel toDetailedModel(Hotel hotel){
        HotelModel hotelModel = new HotelModel();
        hotelModel.setId(hotel.getId());
        hotelModel.setHotelName(hotel.getHotelName());
        hotelModel.setAreaName(hotel.getArea().getAreaName());

        List<RoomType> roomTypes = hotel.getRoomTypes();
        if (roomTypes != null){
            if (!roomTypes.isEmpty()){
                hotelModel.setRoomTypeModels(roomTypeService.convertToRoomTypeModels(roomTypes));
            }
        }

        List<HotelHall> hotelHalls = hotel.getHotelHalls();
        if (hotelHalls != null){
            if (!hotelHalls.isEmpty()){
                hotelModel.setHotelHallModels(hotelHallService.convertToModels(hotelHalls));
            }
        }

        List<Hotel_Img> hotelImgs = hotel.getHotelImgs();
        if (hotelImgs != null){
            if (!hotelImgs.isEmpty()){
                if (hotel.getHotelImgs().size() >= 2){
                    hotelModel.setImgUrl(new String(hotel.getHotelImgs().get(1).getImg(), StandardCharsets.UTF_8));
                }
            }
        }

        return hotelModel;
    }
}