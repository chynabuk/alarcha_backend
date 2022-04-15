package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.repositories.ObjectRepository;
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

    @Autowired
    private ObjectRepository objectRepository;

    @Override
    public ObjectModel createObject(ObjectModel objectModel) {
        return null;
    }

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

    @Override
    public ObjectModel getById(Long objectId) {
        return null;
    }

    @Override
    public List<ObjectModel> getByObjectType(ObjectType objectType) {
        return null;
    }

    @Override
    public List<ObjectModel> getAll() {
        List<ObjectModel> objectModels = new ArrayList<>();

        for(Object object : objectRepository.findAll()){
            if(!object.getIsDeleted()){
                objectModels.add(toModel(object));
            }
        }

        return objectModels;
    }

    @Override
    public ObjectModel updateObject(ObjectModel objectModel) {
        return null;
    }

    @Override
    public ObjectModel deleteObject(Long objectId) {
        return null;
    }

    private ObjectModel toModel(Object object){
        ObjectModel objectModel = new ObjectModel();
        objectModel.setId(object.getId());
        objectModel.setName(object.getName());
        objectModel.setObjectTypeName(object.getObjectType().getName());

        return objectModel;
    }
}
