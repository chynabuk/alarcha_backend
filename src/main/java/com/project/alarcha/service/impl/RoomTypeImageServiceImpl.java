package com.project.alarcha.service.impl;

import com.project.alarcha.entities.RoomTypeImage;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.RoomModel.RoomTypeImageModel;
import com.project.alarcha.repositories.RoomTypeImageRepository;
import com.project.alarcha.service.RoomTypeImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoomTypeImageServiceImpl implements RoomTypeImageService {
    @Autowired
    private RoomTypeImageRepository roomTypeImageRepository;

    @Override
    public List<RoomTypeImage> uploadImages(List<RoomTypeImageModel> roomTypeImageModels) {
        List<RoomTypeImage> roomTypeImages = new ArrayList<>();

        for (RoomTypeImageModel roomTypeImageModel : roomTypeImageModels){
            RoomTypeImage roomTypeImage = new RoomTypeImage();
            roomTypeImage.setImg(roomTypeImageModel.getImg().getBytes(StandardCharsets.UTF_8));
            roomTypeImage.setIsDeleted(false);

            roomTypeImages.add(roomTypeImage);
        }

        return roomTypeImages;
    }

    @Override
    public List<RoomTypeImageModel> convertToModels(List<RoomTypeImage> roomTypeImages) {
        List<RoomTypeImageModel> roomTypeImageModels = new ArrayList<>();

        roomTypeImages.forEach(roomTypeImage ->
                roomTypeImageModels.add(toModel(roomTypeImage)));

        return roomTypeImageModels;
    }

    @Override
    public RoomTypeImageModel deleteImage(Long id) {
        RoomTypeImage roomTypeImage = getRoomTypeImage(id);

        roomTypeImage.setIsDeleted(true);

        roomTypeImageRepository.save(roomTypeImage);

        return toModel(roomTypeImage);
    }

    @Override
    public RoomTypeImageModel updateImage(Long id) {
        return null;
    }

    private RoomTypeImage getRoomTypeImage(Long id){
        RoomTypeImage roomTypeImage = roomTypeImageRepository.getById(id);

        if (roomTypeImage == null){
            throw new ApiFailException("RoomTypeImage is not found");
        }

        if (roomTypeImage.getIsDeleted()){
            throw new ApiFailException("RoomTypeImage is not found or deleted");
        }

        return roomTypeImage;
    }

    private RoomTypeImageModel toModel(RoomTypeImage roomTypeImage){
        RoomTypeImageModel roomTypeImageModel = new RoomTypeImageModel();
        roomTypeImageModel.setId(roomTypeImage.getId());
        roomTypeImageModel.setImg(new String(roomTypeImage.getImg(), StandardCharsets.UTF_8));
        roomTypeImageModel.setRoomTypeId(roomTypeImage.getRoomType().getId());

        return roomTypeImageModel;
    }
}
