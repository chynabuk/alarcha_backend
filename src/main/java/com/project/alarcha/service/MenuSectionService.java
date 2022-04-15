package com.project.alarcha.service;

import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.models.MenuModel.MenuSectionModel;

import java.util.List;

public interface MenuSectionService {
    List<MenuSection> createMenuSections(List<MenuSectionModel> menuSectionModels);
}
