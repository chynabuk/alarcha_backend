package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomOrder;
import com.project.alarcha.entities.RoomType;
import com.project.alarcha.enums.RoomStatus;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.repositories.RoomRepository;
import com.project.alarcha.repositories.RoomTypeRepository;
import com.project.alarcha.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
    public RoomModel createRoom(RoomModel roomModel) {
        Room room = new Room();
        RoomType roomType = roomTypeRepository.getById(roomModel.getRoomTypeId());
        room.setRoomNumber(roomModel.getRoomNumber());
        room.setRoomStatus(RoomStatus.ACTIVE);
        room.setBedNumber(roomModel.getBedNumber());
        room.setRoomType(roomType);
        room.setIsDeleted(false);

        roomRepository.save(room);

        return toModel(room);
    }

    @Override
    public List<Room> createRooms(List<RoomModel> roomModels) {
        List<Room> rooms = new ArrayList<>();

        for (RoomModel roomModel : roomModels){
            Room room = new Room();
            room.setRoomNumber(roomModel.getRoomNumber());
            room.setRoomStatus(roomModel.getRoomStatus());
            room.setBedNumber(roomModel.getBedNumber());

            System.out.println("Room");
            System.out.println(roomModel.getHotelName());
            room.setIsDeleted(false);

            rooms.add(room);
        }

        return rooms;
    }


    @Override
    public List<Room> convertToRoom(List<RoomModel> roomModels) {
//        List<Room> rooms = new ArrayList<>();
//
//        for (RoomModel roomModel : roomModels){
//            Room room = new Room();
//            room.setRoomNumber(roomModel.getRoomNumber());
//            room.setRoomStatus(roomModel.getRoomStatus());
//            room.setRoomType(roomModel.getRoomType());
//            room.setBedNumber(roomModel.getBedNumber());
//            room.setPrice(roomModel.getPrice());
//
//            rooms.add(room);
//        }

        return null;
    }

    @Override
    public RoomModel getById(Long roomId) {
        Room room = getRoom(roomId);
        return toModel(room);
    }

    @Override
    public Room getByRoomId(Long roomId) {
        return getRoom(roomId);
    }

    @Override
    public List<RoomModel> getByRoomType(RoomType roomType) {
        List<RoomModel> roomModels = new ArrayList<>();

        for (Room room : roomType.getRooms()){
            if (!room.getIsDeleted()){
                roomModels.add(toModel(room));
            }
        }

        return roomModels;
    }

    @Override
    public List<RoomModel> getAll() {
        List<RoomModel> roomModels = new ArrayList<>();

        for (Room room : roomRepository.findAll()){
            if (!room.getIsDeleted()){
                roomModels.add(toModel(room));
            }
        }

        return roomModels;
    }

    @Override
    public RoomModel updateRoom(RoomModel roomModel) {
        Room room = getRoom(roomModel.getId());

        setValuesOnUpdateRoom(room, roomModel);

        roomRepository.save(room);

        return toModel(room);
    }

    @Override
    public RoomModel deleteRoom(Long roomId) {
        Room room = getRoom(roomId);

        room.setIsDeleted(true);

        for (RoomOrder roomOrder : room.getRoomOrders()){
            if (!roomOrder.getIsDeleted()){
                roomOrder.setIsDeleted(true);
            }
        }

        roomRepository.save(room);

        return toModel(room);
    }

    private Room getRoom(Long id){
        Room room = roomRepository.getById(id);

        if (room != null){
            throw new ApiFailException("Room is not found");
        }
        if (room.getIsDeleted()){
            throw new ApiFailException("Room is not found or deleted");
        }

        return room;
    }

    private void setValuesOnUpdateRoom(Room room, RoomModel roomModel){
//        RoomType roomType = roomModel.getRoomType();
//        if (roomType != null){
//            room.setRoomType(roomModel.getRoomType());
//        }
//        if (price != null){
//            room.setPrice(roomModel.getPrice());
//        }
    }

    private RoomModel toModel(Room room){
        RoomModel roomModel = new RoomModel();
        roomModel.setId(room.getId());
        roomModel.setRoomNumber(room.getRoomNumber());
        roomModel.setRoomStatus(room.getRoomStatus());
        roomModel.setBedNumber(room.getBedNumber());
        roomModel.setHotelName(room.getRoomType().getHotel().getHotelName());

        return roomModel;
    }
}
