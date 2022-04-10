package com.project.alarcha.service;

import com.project.alarcha.entities.Room;
import com.project.alarcha.models.RoomModel.RoomModel;

import java.util.List;

public interface RoomService {
    RoomModel createRoom(RoomModel roomModel);
    List<RoomModel> createRooms(List<RoomModel> roomModels);
    List<Room> convertToRoom(List<RoomModel> roomModels);
}
