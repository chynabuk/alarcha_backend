package com.project.alarcha.service;

import com.project.alarcha.entities.Object;
import com.project.alarcha.models.ObjectModel.ObjectModel;

import java.util.List;

public interface ObjectService {
    List<Object> createObjects(List<ObjectModel> objectModels);
    List<Object> convertToObjects(List<ObjectModel> objectModels);
}
