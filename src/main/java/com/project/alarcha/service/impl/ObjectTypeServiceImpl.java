package com.project.alarcha.service.impl;

import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import com.project.alarcha.repositories.ObjectTypeRepository;
import com.project.alarcha.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private AreaService areaService;

    @Override
    public ObjectTypeModel createObjectType(ObjectTypeModel objectTypeModel) {
        ObjectType objectType = new ObjectType();

        objectTypeRepository.save(initAndGet(objectType, objectTypeModel));

        return objectTypeModel;
    }

    @Override
    public ObjectTypeModel getById(Long objectTypeId) {
        ObjectType objectType = objectTypeRepository.getById(objectTypeId);
        return toModel(objectType);
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
    public ObjectTypeModel updateObjectType(ObjectTypeModel objectTypeModel) {
        return null;
    }

    @Override
    public ObjectTypeModel deleteObjectType(Long ObjectTypeId) {
        return null;
    }

    private ObjectType initAndGet(ObjectType objectType, ObjectTypeModel objectTypeModel){
        objectType.setName(objectTypeModel.getName());
        objectType.setPrice(objectTypeModel.getPrice());
        objectType.setArea(areaService.getById(objectTypeModel.getAreaId()));

        List<MenuSectionModel> menuSectionModels = objectTypeModel.getMenuSectionModels();
        menuSectionModels.forEach(menuSectionModel -> menuSectionModel.setObjectTypeName(objectTypeModel.getName()));

        List<MenuSection> menuSections = menuSectionService.createMenuSections(menuSectionModels);
        objectType.setMenuSections(menuSections);
        menuSections.forEach(menuSection -> menuSection.setObjectType(objectType));

        List<Object> objects = objectService.createObjects(objectTypeModel.getObjectModels());
        objectType.setObjects(objects);
        objects.forEach(object -> object.setObjectType(objectType));

        objectType.setIsDeleted(false);

        return objectType;
    }

    private ObjectTypeModel toModel(ObjectType objectType){
        ObjectTypeModel objectTypeModel = new ObjectTypeModel();
        objectTypeModel.setId(objectType.getId());
        objectTypeModel.setName(objectType.getName());
        objectTypeModel.setPrice(objectType.getPrice());
        objectTypeModel.setMenuSectionModels(menuSectionService.getAll());
        objectTypeModel.setObjectModels(objectService.getAll());
        objectTypeModel.setAreaName(objectType.getArea().getAreaName());

        return objectTypeModel;
    }
}
