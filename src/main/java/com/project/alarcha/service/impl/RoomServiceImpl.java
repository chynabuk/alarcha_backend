package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Room;
import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.repositories.RoomRepository;
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

    @Override
    public RoomModel createRoom(RoomModel roomModel) {
        return null;
    }

    @Override
    public List<RoomModel> createRooms(List<RoomModel> roomModels) {
//        for (RoomModel roomModel : roomModels){
//            Room room = new Room();
//            room.setRoomNumber(roomModel.getRoomNumber());
//            room.setRoomStatus(roomModel.getRoomStatus());
//            room.setRoomType(roomModel.getRoomType());
//            room.setBedNumber(roomModel.getBedNumber());
//            room.setPrice(roomModel.getPrice());
//        }
        return null;
    }

    @Override
    public List<Room> convertToRoom(List<RoomModel> roomModels) {
        List<Room> rooms = new ArrayList<>();

        for (RoomModel roomModel : roomModels){
            Room room = new Room();
            room.setRoomNumber(roomModel.getRoomNumber());
            room.setRoomStatus(roomModel.getRoomStatus());
            room.setRoomType(roomModel.getRoomType());
            room.setBedNumber(roomModel.getBedNumber());
            room.setPrice(roomModel.getPrice());

            rooms.add(room);
        }

        return rooms;
    }


}
