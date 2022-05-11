package com.project.alarcha.service.impl;

import com.project.alarcha.entities.*;
import com.project.alarcha.entities.Object;
import com.project.alarcha.enums.TimeType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.models.ObjectModel.ObjectModel;
import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import com.project.alarcha.repositories.AreaRepository;
import com.project.alarcha.repositories.ObjectTypeRepository;
import com.project.alarcha.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ObjectTypeServiceImpl implements ObjectTypeService {
    @Autowired
    private ObjectTypeRepository objectTypeRepository;

    @Autowired
    private ObjectService objectService;

    @Autowired
    private MenuSectionService menuSectionService;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ObjectTypeImgService objectTypeImgService;

    @Override
    public ObjectTypeModel createObjectType(ObjectTypeModel objectTypeModel) {
        ObjectType objectType = new ObjectType();

        objectTypeRepository.save(initAndGet(objectType, objectTypeModel));

        return objectTypeModel;
    }

    @Override
    public ObjectTypeModel getById(Long objectTypeId) {
        ObjectType objectType = getObjectType(objectTypeId);
        return toDetailedModel(objectType);
    }

    @Override
    public List<ObjectTypeModel> getAll() {
        List<ObjectTypeModel> objectTypeModels = new ArrayList<>();

        for(ObjectType objectType : objectTypeRepository.findAll()){
            if(!objectType.getIsDeleted()){
                objectTypeModels.add(toModel(objectType));
            }
        }

        return objectTypeModels;
    }

    @Override
    public List<ObjectTypeModel> getForSelect() {
        List<ObjectTypeModel> objectTypeModels = new ArrayList<>();

        for(ObjectType objectType : objectTypeRepository.findAll()){
            if(!objectType.getIsDeleted()){
                ObjectTypeModel objectTypeModel = new ObjectTypeModel();
                objectTypeModel.setId(objectType.getId());
                objectTypeModel.setName(objectType.getName());
                objectTypeModels.add(objectTypeModel);
            }
        }

        return objectTypeModels;
    }

    @Override
    public List<ObjectTypeModel> getForList() {
        List<ObjectTypeModel> objectTypeModels = new ArrayList<>();

        for(ObjectType objectType : objectTypeRepository.findAll()){
            if(!objectType.getIsDeleted()){
                ObjectTypeModel objectTypeModel = new ObjectTypeModel();
                objectTypeModel.setId(objectType.getId());
                objectTypeModel.setName(objectType.getName());
                objectTypeModel.setAreaName(objectType.getArea().getAreaName());
                objectTypeModel.setTimeType(objectType.getTimeType());
                objectTypeModel.setPrice(objectType.getPrice());
                objectTypeModels.add(objectTypeModel);
            }
        }

        return objectTypeModels;
    }

    @Override
    public ObjectTypeModel updateObjectType(ObjectTypeModel objectTypeModel) {
        return null;
    }

    @Override
    public ObjectTypeModel deleteObjectType(Long objectTypeId) {
        ObjectType objectType = objectTypeRepository.getById(objectTypeId);

        if(objectType != null){
            if(objectType.getIsDeleted()){
                throw new ApiFailException("ObjectType is already deleted!");
            }

            for(MenuSection menuSection : objectType.getMenuSections()){
                for(Menu menu : menuSection.getMenus()){
                    menu.setIsDeleted(true);
                }
                menuSection.setIsDeleted(true);
            }

            for(Object object : objectType.getObjects()){
                object.setIsDeleted(true);
            }

            objectType.setIsDeleted(true);
        }

        objectTypeRepository.save(objectType);

        return toModel(objectType);
    }

    @Override
    public List<ObjectTypeModel> convertToModels(List<ObjectType> objectTypes) {
        List<ObjectTypeModel> objectTypeModels = new ArrayList<>();

        for (ObjectType objectType : objectTypes){
            if (!objectType.getIsDeleted()){
                objectTypeModels.add(toModel(objectType));
            }
        }

        return objectTypeModels;
    }

    private ObjectType getObjectType(Long id){
        ObjectType objectType = objectTypeRepository.findById(id)
                .orElseThrow(() -> new ApiFailException("ObjectType not found"));

        if (objectType.getIsDeleted()){
            throw new ApiFailException("ObjectType is deleted");
        }

        return objectType;

    }

    private ObjectType initAndGet(ObjectType objectType, ObjectTypeModel objectTypeModel){
        objectType.setName(objectTypeModel.getName());
        objectType.setPrice(objectTypeModel.getPrice());
        if (objectTypeModel.getPricePerHour() != null){
            objectType.setPricePerHour(objectTypeModel.getPricePerHour());
        }
        else objectType.setPricePerHour(objectTypeModel.getPrice());

        objectType.setArea(areaRepository.getById(objectTypeModel.getAreaId()));
        objectType.setTimeType(objectTypeModel.getTimeType());

        if(objectType.getTimeType() == TimeType.TIME){
            objectType.setPricePerHour(objectTypeModel.getPricePerHour());
        }

        List<MenuSectionModel> menuSectionModels = objectTypeModel.getMenuSectionModels();
        if(menuSectionModels != null){
            menuSectionModels.forEach(menuSectionModel -> menuSectionModel.setObjectTypeName(objectTypeModel.getName()));

            List<MenuSection> menuSections = menuSectionService.createMenuSections(menuSectionModels);
            if(menuSections != null){
                objectType.setMenuSections(menuSections);
                menuSections.forEach(menuSection -> menuSection.setObjectType(objectType));
            }
        }

        List<ObjectModel> objectModels = objectTypeModel.getObjectModels();
        if(objectModels != null) {
            objectModels.forEach(objectModel -> objectModel.setObjectTypeName(objectTypeModel.getName()));

            List<Object> objects = objectService.createObjects(objectTypeModel.getObjectModels());
            if (objects != null) {
                objectType.setObjects(objects);
                objects.forEach(object -> object.setObjectType(objectType));
            }

        }

        if (objectTypeModel.getObjectTypeImgModels() != null) {
            List<ObjectTypeImage> objectTypeImages = objectTypeImgService.uploadImages(objectTypeModel.getObjectTypeImgModels());
            objectType.setObjectTypeImages(objectTypeImages);
            objectTypeImages.forEach(objectTypeImage -> objectTypeImage.setObjectType(objectType));
        }

        objectType.setIsDeleted(false);

        return objectType;
    }

    private ObjectTypeModel toModel(ObjectType objectType){
        ObjectTypeModel objectTypeModel = new ObjectTypeModel();
        objectTypeModel.setId(objectType.getId());
        objectTypeModel.setName(objectType.getName());
        objectTypeModel.setPrice(objectType.getPrice());
        if (!objectType.getObjectTypeImages().isEmpty()){
            objectTypeModel.setImgUrl(new String(objectType.getObjectTypeImages().get(0).getImg(), StandardCharsets.UTF_8));
        }
        return objectTypeModel;
    }

    private ObjectTypeModel toDetailedModel(ObjectType objectType){
        ObjectTypeModel objectTypeModel = new ObjectTypeModel();
        objectTypeModel.setId(objectType.getId());
        objectTypeModel.setName(objectType.getName());
        objectTypeModel.setPrice(objectType.getPrice());
        objectTypeModel.setPricePerHour(objectType.getPricePerHour());
        if (!objectType.getMenuSections().isEmpty()){
            objectTypeModel.setMenuSectionModels(menuSectionService.getByObjectType(objectType));
        }
        if (!objectType.getObjects().isEmpty()){
            objectTypeModel.setObjectModels(objectService.getByObjectType(objectType));
        }
        if (!objectType.getObjectTypeImages().isEmpty()){
            if (objectType.getObjectTypeImages().size() > 2){
                objectTypeModel.setObjectTypeImgModels(objectTypeImgService
                        .convertToModels(objectType.getObjectTypeImages()
                        .subList(1, objectType.getObjectTypeImages().size() - 1)));
            }
        }
        objectTypeModel.setAreaName(objectType.getArea().getAreaName());
        objectTypeModel.setTimeType(objectType.getTimeType());

        return objectTypeModel;
    }
}
