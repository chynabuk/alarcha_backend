package com.project.alarcha.service;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomType;
import com.project.alarcha.models.RoomModel.RoomModel;

import java.util.List;

public interface RoomService {
    RoomModel createRoom(RoomModel roomModel);
    List<Room> createRooms(List<RoomModel> roomModels);
    List<Room> convertToRoom(List<RoomModel> roomModels);
    RoomModel getById(Long roomId);
    List<RoomModel> getByRoomType(RoomType roomType);
    List<RoomModel> getAll();
    RoomModel updateRoom(RoomModel roomModel);
    RoomModel deleteRoom(Long roomId);
}
