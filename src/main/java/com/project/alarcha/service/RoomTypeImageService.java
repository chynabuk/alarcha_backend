package com.project.alarcha.service;


import com.project.alarcha.entities.RoomTypeImage;
import com.project.alarcha.models.RoomModel.RoomTypeImageModel;

import java.util.List;

public interface RoomTypeImageService {
    List<RoomTypeImage> uploadImages(List<RoomTypeImageModel> roomTypeImageModels);
    List<RoomTypeImageModel> convertToModels(List<RoomTypeImage> roomTypeImages);
    RoomTypeImageModel deleteImage(Long id);
    RoomTypeImageModel updateImage(Long id);
}
