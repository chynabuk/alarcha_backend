package com.project.alarcha.service;

import com.project.alarcha.entities.Object;
import com.project.alarcha.models.ObjectModel.ObjectModel;

import java.util.List;

public interface ObjectService {
    ObjectModel createObject(Object object);
    List<Object> convertToObjects(List<ObjectModel> objectModels);
}
