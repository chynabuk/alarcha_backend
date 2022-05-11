package com.project.alarcha.service;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.models.ObjectModel.ObjectModel;

import java.util.List;

public interface ObjectService {
    ObjectModel createObject(ObjectModel objectModel);
    List<Object> createObjects(List<ObjectModel> objectModels);
    List<Object> convertToObjects(List<ObjectModel> objectModels);
    ObjectModel getById(Long objectId);
    Object getByObjectId(Long objectId);
    List<ObjectModel> getByObjectType(ObjectType objectType);
    List<ObjectModel> getAll();
    List<ObjectModel> getForList();
    ObjectModel updateObject(ObjectModel objectModel);
    ObjectModel deleteObject(Long objectId);
}
