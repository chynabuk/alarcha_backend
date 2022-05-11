package com.project.alarcha.service;

import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.models.ObjectModel.ObjectModel;

import java.util.List;

public interface MenuSectionService {
    MenuSectionModel createMenuSection(MenuSectionModel menuSectionModel);
    List<MenuSection> createMenuSections(List<MenuSectionModel> menuSectionModels);
    MenuSectionModel getById(Long menuSectionId);
    List<MenuSectionModel> getAll();
    List<MenuSectionModel> getForSelect();
    List<MenuSectionModel> getForList();
    List<MenuSectionModel> getByObjectType(ObjectType objectType);
    MenuSectionModel updateMenuSection(MenuSectionModel menuSectionModel);
    MenuSectionModel deleteMenuSection(Long menuSectionId);
}
