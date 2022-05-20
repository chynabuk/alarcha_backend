package com.project.alarcha.service.impl;

import com.project.alarcha.entities.ObjectTypeImage;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.ObjectModel.ObjectTypeImgModel;
import com.project.alarcha.repositories.ObjectTypeImgRepository;
import com.project.alarcha.service.ObjectTypeImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ObjectTypeImgServiceImpl implements ObjectTypeImgService {
    @Autowired
    private ObjectTypeImgRepository objectTypeImgRepository;

    @Override
    public List<ObjectTypeImage> uploadImages(List<ObjectTypeImgModel> objectTypeImgModels) {
        List<ObjectTypeImage> objectTypeImages = new ArrayList<>();

        for (ObjectTypeImgModel objectTypeImgModel : objectTypeImgModels){
            ObjectTypeImage objectTypeImage = new ObjectTypeImage();
            objectTypeImage.setImg(objectTypeImgModel.getImg().getBytes(StandardCharsets.UTF_8));
            objectTypeImage.setIsDeleted(false);
            objectTypeImage.setDeletedDate(new Date());

            objectTypeImages.add(objectTypeImage);
        }

        return objectTypeImages;
    }

    @Override
    public List<ObjectTypeImgModel> convertToModels(List<ObjectTypeImage> objectTypeImages) {
        List<ObjectTypeImgModel> objectTypeImgModels = new ArrayList<>();

        objectTypeImages.forEach(objectTypeImage ->
                objectTypeImgModels.add(toModel(objectTypeImage)));

        return objectTypeImgModels;
    }

    @Override
    public ObjectTypeImgModel deleteImage(Long id) {
        ObjectTypeImage objectTypeImage = objectTypeImgRepository.getById(id);

        if (objectTypeImage == null){
            throw new ApiFailException("ObjectTypeImage не найден.");
        }

        if (objectTypeImage.getIsDeleted()){
            throw new ApiFailException("ObjectTypeImage уже удален.");
        }

        objectTypeImage.setIsDeleted(true);

        objectTypeImgRepository.save(objectTypeImage);

        return toModel(objectTypeImage);
    }

    @Override
    public ObjectTypeImgModel updateImage(Long id) {
        return null;
    }

    private ObjectTypeImgModel toModel(ObjectTypeImage objectTypeImage){
        ObjectTypeImgModel objectTypeImgModel = new ObjectTypeImgModel();
        objectTypeImgModel.setId(objectTypeImage.getId());
        objectTypeImgModel.setImg(new String(objectTypeImage.getImg(), StandardCharsets.UTF_8));
        objectTypeImgModel.setRoomTypeId(objectTypeImage.getObjectType().getId());

        return objectTypeImgModel;
    }
}
