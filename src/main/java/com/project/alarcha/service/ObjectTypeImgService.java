package com.project.alarcha.service;

import com.project.alarcha.entities.ObjectTypeImage;
import com.project.alarcha.models.ObjectModel.ObjectTypeImgModel;

import java.util.List;

public interface ObjectTypeImgService {
    List<ObjectTypeImage> uploadImages(List<ObjectTypeImgModel> ObjectTypeImgModels);
    List<ObjectTypeImgModel> convertToModels(List<ObjectTypeImage> objectTypeImages);
    ObjectTypeImgModel deleteImage(Long id);
    ObjectTypeImgModel updateImage(Long id);
}
