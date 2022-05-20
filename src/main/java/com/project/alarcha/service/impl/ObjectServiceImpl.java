package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectOrder;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.repositories.ObjectRepository;
import com.project.alarcha.repositories.ObjectTypeRepository;
import com.project.alarcha.service.ObjectOrderService;
import com.project.alarcha.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ObjectServiceImpl implements ObjectService {
    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private ObjectTypeRepository objectTypeRepository;

    @Autowired
    private ObjectOrderService objectOrderService;

    @Override
    public ObjectModel createObject(ObjectModel objectModel) {
        Object object = new Object();
        ObjectType objectType = objectTypeRepository.getById(objectModel.getObjectTypeId());
        object.setName(objectModel.getName());
        object.setNumberOfSeats(objectModel.getNumberOfSeats());
        object.setObjectType(objectType);
        object.setIsDeleted(false);

        objectRepository.save(object);
        return objectModel;
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
        Object object = getObject(objectId);

        return toModel(object);
    }

    @Override
    public Object getByObjectId(Long objectId) {
        return objectRepository.getById(objectId);
    }

    @Override
    public List<ObjectModel> getByObjectType(ObjectType objectType) {
        List<ObjectModel> objectModels = new ArrayList<>();

        List<Object> objects = objectType.getObjects();
        if (objects != null){
            for(Object object : objectType.getObjects()){
                if(!object.getIsDeleted()){
                    objectModels.add(toModel(object));
                }
            }
        }

        return objectModels;
    }

    @Override
    public List<ObjectModel> getAll() {
        List<ObjectModel> objectModels = new ArrayList<>();

        for(Object object : objectRepository.getAll()){
            objectModels.add(toModel(object));
        }

        return objectModels;
    }

    @Override
    public List<ObjectModel> getForList() {
        List<ObjectModel> objectModels = new ArrayList<>();

        for(Object object : objectRepository.getAll()){
            ObjectModel objectModel = new ObjectModel();
            objectModel.setId(object.getId());
            objectModel.setName(object.getName());
            objectModel.setObjectTypeName(object.getObjectType().getName());
            objectModel.setNumberOfSeats(object.getNumberOfSeats());
            objectModels.add(objectModel);
        }

        return objectModels;
    }

    @Override
    public ObjectModel updateObject(ObjectModel objectModel) {
        Object object = getObject(objectModel.getId());

        setValuesOnUpdateObject(object, objectModel);

        objectRepository.save(object);

        return objectModel;
    }

    @Override
    public ObjectModel deleteObject(Long objectId) {
        Object object = getObject(objectId);

        Date deletedDate = new Date();
        object.setIsDeleted(true);
        object.setDeletedDate(deletedDate);

        List<ObjectOrder> objectOrders = object.getObjectOrders();
        if (objectOrders != null){
            if (!objectOrders.isEmpty()){
                for(ObjectOrder objectOrder : objectOrders){
                    if(!objectOrder.getIsDeleted()){
                        objectOrder.setIsDeleted(true);
                        objectOrder.setDeletedDate(deletedDate);
                    }
                }
            }
        }

        objectRepository.save(object);

        return toModel(object);
    }

    private Object getObject(Long objectId){
        Object object = objectRepository
                .findById(objectId)
                .orElseThrow(() -> new ApiFailException("Объект не найден."));

        if(object.getIsDeleted()){
            throw new ApiFailException("Объект не найден или удален.");
        }

        return object;
    }

    private void setValuesOnUpdateObject(Object object, ObjectModel objectModel){
        String name = objectModel.getName();
        Integer numberOfSeats = objectModel.getNumberOfSeats();

        if(name != null){
            object.setName(name);
        }

        if(numberOfSeats != null){
            object.setNumberOfSeats(numberOfSeats);
        }
    }

    private ObjectModel toModel(Object object){
        ObjectModel objectModel = new ObjectModel();
        objectModel.setId(object.getId());
        objectModel.setName(object.getName());
        objectModel.setObjectTypeName(object.getObjectType().getName());
        objectModel.setNumberOfSeats(object.getNumberOfSeats());
        objectModel.setObjectTypeId(object.getObjectType().getId());

        List<ObjectOrder> objectOrders = object.getObjectOrders();
        if (objectOrders != null){
            if (!objectOrders.isEmpty()){
                objectModel.setObjectOrderModels(objectOrderService.convertToModels(objectOrders));
            }
        }

        return objectModel;
    }
}
