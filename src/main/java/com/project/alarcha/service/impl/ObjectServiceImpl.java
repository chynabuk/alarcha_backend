package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.service.AreaService;
import com.project.alarcha.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ObjectServiceImpl implements ObjectService {
    @Autowired
    private AreaService areaService;

    @Override
    public List<Object> createObjects(List<ObjectModel> objectModels) {
        List<Object> objects = new ArrayList<>();

        for(ObjectModel objectModel : objectModels){
            Object object = new Object();
            object.setName(objectModel.getName());
            object.setIsDeleted(false);

            objects.add(object);
        }
        return objects;
    }

    @Override
    public List<Object> convertToObjects(List<ObjectModel> objectModels) {
        return null;
    }
}
