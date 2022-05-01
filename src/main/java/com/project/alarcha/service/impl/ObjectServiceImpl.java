package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.repositories.ObjectRepository;
import com.project.alarcha.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ObjectServiceImpl implements ObjectService {
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
            object.setNumberOfSeats(objectModel.getNumberOfSeats());
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
        Object object = objectRepository.getById(objectId);
        return toModel(object);
    }

    @Override
    public Object getByObjectId(Long objectId) {
        return objectRepository.getById(objectId);
    }

    @Override
    public List<ObjectModel> getByObjectType(ObjectType objectType) {
        List<ObjectModel> objectModels = new ArrayList<>();

        for(Object object : objectType.getObjects()){
            if(!object.getIsDeleted()){
                objectModels.add(toModel(object));
            }
        }

        return objectModels;
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
        Object object = objectRepository.getById(objectId);

        if(object != null){
            if(object.getIsDeleted()){
                throw new ApiFailException("Object is already deleted!");
            }

            object.setIsDeleted(true);
        }

        objectRepository.save(object);

        return toModel(object);
    }

    private ObjectModel toModel(Object object){
        ObjectModel objectModel = new ObjectModel();
        objectModel.setId(object.getId());
        objectModel.setName(object.getName());
        objectModel.setObjectTypeName(object.getObjectType().getName());

        return objectModel;
    }
}
