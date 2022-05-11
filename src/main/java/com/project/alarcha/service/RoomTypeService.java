package com.project.alarcha.service;

import com.project.alarcha.entities.RoomType;
import com.project.alarcha.models.RoomModel.RoomModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;

import java.util.List;

public interface RoomTypeService {
    RoomTypeModel createRoomType(RoomTypeModel roomTypeModel);
    List<RoomType> createRoomTypes(List<RoomTypeModel> roomTypeModels);
    List<RoomType> convertToRoomType(List<RoomModel> roomModels);
    List<RoomTypeModel> convertToRoomTypeModels(List<RoomType> roomTypes);
    RoomTypeModel getById(Long roomTypeId);
    List<RoomTypeModel> getAll();
    List<RoomTypeModel> getForSelect();
    List<RoomTypeModel> getForList();
    RoomTypeModel updateRoomType(RoomTypeModel roomTypeModel);
    RoomTypeModel deleteRoomType(Long roomTypeId);
}
