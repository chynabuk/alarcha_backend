package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomType;
import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;
import com.project.alarcha.repositories.RoomTypeRepository;
import com.project.alarcha.service.RoomService;
import com.project.alarcha.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomService roomService;

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
    public RoomTypeModel getById(Long roomTypeId) {
        return null;
    }

    @Override
    public List<RoomTypeModel> getAll() {
        return null;
    }

    @Override
    public RoomTypeModel updateRoomType(RoomTypeModel roomTypeModel) {
        return null;
    }

    @Override
    public RoomTypeModel deleteRoomType(Long roomTypeId) {
        return null;
    }
}
