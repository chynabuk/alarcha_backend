package com.project.alarcha.service;

import com.project.alarcha.models.ObjectModel.ObjectTypeModel;

import java.util.List;

public interface ObjectTypeService {
    ObjectTypeModel createObjectType(ObjectTypeModel objectTypeModel);
    ObjectTypeModel getById(Long objectTypeId);
    List<ObjectTypeModel> getAll();
    ObjectTypeModel updateObjectType(ObjectTypeModel objectTypeModel);
    ObjectTypeModel deleteObjectType(Long objectTypeId);
}
