package com.project.alarcha.service;

import com.project.alarcha.entities.Room;
import com.project.alarcha.entities.RoomType;
import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;

import java.util.List;

public interface RoomTypeService {
    List<RoomType> createRoomTypes(List<RoomTypeModel> roomTypeModels);
    List<RoomType> convertToRoomType(List<RoomModel> roomModels);
    RoomTypeModel getById(Long roomTypeId);
    List<RoomTypeModel> getAll();
    RoomTypeModel updateRoomType(RoomTypeModel roomTypeModel);
    RoomTypeModel deleteRoomType(Long roomTypeId);
}
