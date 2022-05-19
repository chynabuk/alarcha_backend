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

        for(ObjectType objectType : objectTypeRepository.getAll()){
            objectTypeModels.add(toModel(objectType));
        }

        return objectTypeModels;
    }

    @Override
    public List<ObjectTypeModel> getForSelect() {
        List<ObjectTypeModel> objectTypeModels = new ArrayList<>();

        for(ObjectType objectType : objectTypeRepository.getAll()){
            ObjectTypeModel objectTypeModel = new ObjectTypeModel();
            objectTypeModel.setId(objectType.getId());
            objectTypeModel.setName(objectType.getName());
            objectTypeModels.add(objectTypeModel);
        }

        return objectTypeModels;
    }

    @Override
    public List<ObjectTypeModel> getForList() {
        List<ObjectTypeModel> objectTypeModels = new ArrayList<>();

        for(ObjectType objectType : objectTypeRepository.getAll()){
            ObjectTypeModel objectTypeModel = new ObjectTypeModel();
            objectTypeModel.setId(objectType.getId());
            objectTypeModel.setName(objectType.getName());
            objectTypeModel.setAreaName(objectType.getArea().getAreaName());
            objectTypeModel.setTimeType(objectType.getTimeType());
            objectTypeModel.setPrice(objectType.getPrice());
            objectTypeModel.setMinHours(objectType.getMinHours());
            objectTypeModel.setPricePerHour(objectType.getPricePerHour());
            objectTypeModels.add(objectTypeModel);
        }

        return objectTypeModels;
    }

    @Override
    public ObjectTypeModel updateObjectType(ObjectTypeModel objectTypeModel) {
        ObjectType objectType = getObjectType(objectTypeModel.getId());

        setValuesOnUpdateObjectType(objectType, objectTypeModel);

        objectTypeRepository.save(objectType);

        return objectTypeModel;
    }

    @Override
    public ObjectTypeModel deleteObjectType(Long objectTypeId) {
        ObjectType objectType = getObjectType(objectTypeId);

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

    private ObjectType getObjectType(Long objectTypeId){
        ObjectType objectType = objectTypeRepository
                .findById(objectTypeId)
                .orElseThrow(() -> new ApiFailException("Тип объекта не найден."));

        if(objectType.getIsDeleted()){
            throw new ApiFailException("Тип объекта не найден или удален.");
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
            objectType.setMinHours(objectTypeModel.getMinHours());
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

    private void setValuesOnUpdateObjectType(ObjectType objectType, ObjectTypeModel objectTypeModel){
        String name = objectTypeModel.getName();
        Float price = objectTypeModel.getPrice();

        if(name != null){
            objectType.setName(name);
        }

        if(price != null){
            objectType.setPrice(price);
        }

        if(objectType.getTimeType() == TimeType.TIME){
            Float pricePerHour = objectTypeModel.getPricePerHour();
            Integer minHours = objectTypeModel.getMinHours();

            if(pricePerHour != null){
                objectType.setPricePerHour(pricePerHour);
            }

            if(minHours != null){
                objectType.setMinHours(minHours);
            }
        }
    }

    private ObjectTypeModel toModel(ObjectType objectType){
        ObjectTypeModel objectTypeModel = new ObjectTypeModel();
        objectTypeModel.setId(objectType.getId());
        objectTypeModel.setName(objectType.getName());
        objectTypeModel.setPrice(objectType.getPrice());
        objectTypeModel.setPricePerHour(objectType.getPricePerHour());
        objectTypeModel.setMinHours(objectType.getMinHours());

        objectTypeModel.setMenuSectionModels(menuSectionService.getByObjectType(objectType));
        objectTypeModel.setObjectModels(objectService.getByObjectType(objectType));

        List<ObjectTypeImage> objectTypeImages = objectType.getObjectTypeImages();
        if (objectTypeImages != null){
            if (!objectTypeImages.isEmpty()){
                objectTypeModel.setImgUrl(new String(objectTypeImages.get(0).getImg(), StandardCharsets.UTF_8));
            }
        }
        return objectTypeModel;
    }

    private ObjectTypeModel toDetailedModel(ObjectType objectType){
        ObjectTypeModel objectTypeModel = new ObjectTypeModel();
        objectTypeModel.setId(objectType.getId());
        objectTypeModel.setName(objectType.getName());
        objectTypeModel.setPrice(objectType.getPrice());
        objectTypeModel.setPricePerHour(objectType.getPricePerHour());
        objectTypeModel.setMinHours(objectType.getMinHours());

        List<MenuSection> menuSections = objectType.getMenuSections();
        if (menuSections != null){
            if (!menuSections.isEmpty()){
                objectTypeModel.setMenuSectionModels(menuSectionService.getByObjectType(objectType));
            }
        }

        List<Object> objects = objectType.getObjects();
        if (objects != null){
            if (!objects.isEmpty()){
                objectTypeModel.setObjectModels(objectService.getByObjectType(objectType));
            }
        }

        List<ObjectTypeImage> objectTypeImages = objectType.getObjectTypeImages();
        if (objectTypeImages != null){
            if (!objectTypeImages.isEmpty()){
                if (objectType.getObjectTypeImages().size() > 2){
                    objectTypeModel.setObjectTypeImgModels(objectTypeImgService
                            .convertToModels(objectTypeImages
                                    .subList(1, objectTypeImages.size())));
                }
            }
        }

        objectTypeModel.setAreaName(objectType.getArea().getAreaName());
        objectTypeModel.setTimeType(objectType.getTimeType());

        if(objectType.getTimeType() == TimeType.TIME){
            objectTypeModel.setPricePerHour(objectType.getPricePerHour());
            objectTypeModel.setMinHours(objectType.getMinHours());
        }

        return objectTypeModel;
    }
}
