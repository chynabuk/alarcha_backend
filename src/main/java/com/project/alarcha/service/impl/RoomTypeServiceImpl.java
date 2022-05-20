package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.models.RoomModel.RoomTypeImageModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;
import com.project.alarcha.repositories.HotelRepository;
import com.project.alarcha.repositories.RoomTypeRepository;
import com.project.alarcha.service.RoomService;
import com.project.alarcha.service.RoomTypeImageService;
import com.project.alarcha.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomTypeImageService roomTypeImageService;

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public RoomTypeModel createRoomType(RoomTypeModel roomTypeModel) {
        RoomType roomType = initAndGet(roomTypeModel);
        roomTypeRepository.save(roomType);
        return toModel(roomType);
    }

    @Override
    public List<RoomType> createRoomTypes(List<RoomTypeModel> roomTypeModels) {
        List<RoomType> roomTypes = new ArrayList<>();

        for (RoomTypeModel roomTypeModel : roomTypeModels){
            RoomType roomType = new RoomType();
            roomType.setType(roomTypeModel.getType());
            roomType.setPrice(roomTypeModel.getPrice());
            roomType.setIsDeleted(false);

            List<RoomModel> roomModels = roomTypeModel.getRoomModels();
            roomModels.forEach(roomModel -> roomModel.setHotelName(roomTypeModel.getHotelName()));

            roomType.setRooms(roomService.createRooms(roomModels));
            
            roomTypes.add(roomType);
        }

        for (RoomType roomType : roomTypes){
            List<Room> rooms = roomType.getRooms();

            for (Room room : rooms){
                room.setRoomType(roomType);
            }
        }

        return roomTypes;
    }

    @Override
    public List<RoomType> convertToRoomType(List<RoomModel> roomModels) {
        return null;
    }

    @Override
    public List<RoomTypeModel> convertToRoomTypeModels(List<RoomType> roomTypes) {
        List<RoomTypeModel> roomTypeModels = new ArrayList<>();

        for (RoomType roomType : roomTypes){
            if (!roomType.getIsDeleted()){
                roomTypeModels.add(toModelDetailed(roomType));
            }
        }
        return roomTypeModels;
    }

    @Override
    public RoomTypeModel getById(Long roomTypeId) {
        RoomType roomType = getRoomType(roomTypeId);

        return toModelDetailed(roomType);
    }

    @Override
    public List<RoomTypeModel> getAll() {
        List<RoomTypeModel> roomTypeModels = new ArrayList<>();

        for (RoomType roomType : roomTypeRepository.getAll()){
            roomTypeModels.add(toModel(roomType));
        }

        return roomTypeModels;
    }

    @Override
    public List<RoomTypeModel> getForSelect() {
        List<RoomTypeModel> roomTypeModels = new ArrayList<>();

        for (RoomType roomType : roomTypeRepository.getAll()){
            RoomTypeModel roomTypeModel = new RoomTypeModel();
            roomTypeModel.setId(roomType.getId());
            roomTypeModel.setType(roomType.getType());
            roomTypeModels.add(roomTypeModel);
        }

        return roomTypeModels;
    }

    @Override
    public List<RoomTypeModel> getForList() {
        List<RoomTypeModel> roomTypeModels = new ArrayList<>();

        for (RoomType roomType : roomTypeRepository.getAll()){
            RoomTypeModel roomTypeModel = new RoomTypeModel();
            roomTypeModel.setId(roomType.getId());
            roomTypeModel.setType(roomType.getType());
            roomTypeModel.setHotelName(roomType.getHotel().getHotelName());
            roomTypeModel.setPrice(roomType.getPrice());
            roomTypeModels.add(roomTypeModel);
        }

        return roomTypeModels;
    }

    @Override
    public RoomTypeModel updateRoomType(RoomTypeModel roomTypeModel) {
        RoomType roomType = getRoomType(roomTypeModel.getId());
        String type = roomTypeModel.getType();
        Float price = roomTypeModel.getPrice();

        if (type != null || !type.isEmpty()){
            roomType.setType(type);
        }
        if (price != null){
            roomType.setPrice(price);
        }

        roomTypeRepository.save(roomType);
        return roomTypeModel;
    }

    @Override
    public RoomTypeModel deleteRoomType(Long roomTypeId) {
        RoomType roomType = getRoomType(roomTypeId);

        List<Room> rooms = roomType.getRooms();
        if (rooms != null){
            if (!rooms.isEmpty()){
                for (Room room : rooms){
                    if (!room.getIsDeleted()){
                        room.setIsDeleted(true);
                        List<RoomOrder> roomOrders = room.getRoomOrders();
                        if (roomOrders != null){
                            if (!roomOrders.isEmpty()){
                                for (RoomOrder roomOrder : room.getRoomOrders()){
                                    if (!roomOrder.getIsDeleted()){
                                        roomOrder.setIsDeleted(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        List<RoomTypeImage> roomTypeImages = roomType.getRoomTypeImages();
        if (roomTypeImages != null){
            if (!roomTypeImages.isEmpty()){
                for (RoomTypeImage roomTypeImage : roomTypeImages){
                    if (!roomType.getIsDeleted()){
                        roomTypeImage.setIsDeleted(true);
                    }
                }
            }
        }

        roomType.setIsDeleted(true);

        roomTypeRepository.save(roomType);

        return toModel(roomType);
    }

    private RoomType getRoomType(Long id){
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ApiFailException("Тип комнаты не найден."));

        if (roomType.getIsDeleted()){
            throw new ApiFailException("Тип комнаты не найден или удален.");
        }

        return roomType;
    }

    private RoomType initAndGet(RoomTypeModel roomTypeModel){
        RoomType roomType = new RoomType();

        Hotel hotel = hotelRepository.getById(roomTypeModel.getHotelId());

        if (hotel == null){
            throw new ApiFailException("Невозможно устаносить отель");
        }

        roomType.setType(roomTypeModel.getType());
        roomType.setPrice(roomTypeModel.getPrice());
        roomType.setIsDeleted(false);

        roomType.setHotel(hotel);

        List<RoomModel> roomModels = roomTypeModel.getRoomModels();

        if (roomModels != null){
            if (!roomModels.isEmpty()){
                roomModels.forEach(roomModel -> roomModel.setHotelName(roomTypeModel.getHotelName()));

                List<Room> rooms = roomService.createRooms(roomModels);
                roomType.setRooms(rooms);

                rooms.forEach(room -> room.setRoomType(roomType));
            }
        }

        List<RoomTypeImageModel> roomTypeImageModels = roomTypeModel.getRoomTypeImageModels();
        if (roomTypeImageModels != null){
            if (!roomTypeImageModels.isEmpty()){
                List<RoomTypeImage> roomTypeImages = roomTypeImageService
                        .uploadImages(roomTypeModel
                                .getRoomTypeImageModels());

                roomType.setRoomTypeImages(roomTypeImages);

                roomTypeImages.forEach(roomTypeImage -> roomTypeImage.setRoomType(roomType));
            }
        }

        return roomType;
    }

    private RoomTypeModel toModel(RoomType roomType){
        RoomTypeModel roomTypeModel = new RoomTypeModel();
        roomTypeModel.setId(roomType.getId());
        roomTypeModel.setHotelName(roomType.getHotel().getHotelName());
        roomTypeModel.setType(roomType.getType());
        roomTypeModel.setPrice(roomType.getPrice());

        return roomTypeModel;
    }

    private RoomTypeModel toModelDetailed(RoomType roomType){
        RoomTypeModel roomTypeModel = new RoomTypeModel();
        roomTypeModel.setId(roomType.getId());
        roomTypeModel.setHotelName(roomType.getHotel().getHotelName());
        roomTypeModel.setType(roomType.getType());
        roomTypeModel.setPrice(roomType.getPrice());

        List<Room> rooms = roomType.getRooms();
        if (rooms != null){
            if (!rooms.isEmpty()){
                roomTypeModel.setRoomModels(roomService.getByRoomType(roomType));
            }
        }

        List<RoomTypeImage> roomTypeImages = roomType.getRoomTypeImages();
        if (roomTypeImages != null){
            if (!roomTypeImages.isEmpty()){
                roomTypeModel.setRoomTypeImageModels(roomTypeImageService
                        .convertToModels(roomTypeImages));
            }
        }

        return roomTypeModel;
    }
}
