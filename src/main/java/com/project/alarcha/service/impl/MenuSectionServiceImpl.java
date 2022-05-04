package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.MenuModel.MenuModel;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.repositories.MenuSectionRepository;
import com.project.alarcha.service.MenuSectionService;
import com.project.alarcha.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuSectionServiceImpl implements MenuSectionService {
    @Autowired
    private MenuSectionRepository menuSectionRepository;

    @Autowired
    private MenuService menuService;

    @Override
    public List<MenuSection> createMenuSections(List<MenuSectionModel> menuSectionModels) {
        List<MenuSection> menuSections = new ArrayList<>();

        for(MenuSectionModel menuSectionModel : menuSectionModels){
            MenuSection menuSection = new MenuSection();
            menuSection.setName(menuSectionModel.getName());
            menuSection.setIsDeleted(false);

            List<MenuModel> menuModels = menuSectionModel.getMenuModels();
            menuModels.forEach(menuModel -> menuModel.setObjectTypeName(menuSectionModel.getObjectTypeName()));

            menuSection.setMenus(menuService.createMenus(menuModels));

            menuSections.add(menuSection);
        }

        for(MenuSection menuSection : menuSections){
            List<Menu> menus = menuSection.getMenus();

            for(Menu menu : menus){
                menu.setMenuSection(menuSection);
            }
        }

        return menuSections;
    }

    @Override
    public MenuSectionModel getById(Long menuSectionId) {
        MenuSection menuSection = menuSectionRepository.getById(menuSectionId);
        return toModel(menuSection);
    }

    @Override
    public List<MenuSectionModel> getAll() {
        List<MenuSectionModel> menuSectionModels = new ArrayList<>();

        for(MenuSection menuSection : menuSectionRepository.findAll()){
            if(!menuSection.getIsDeleted()){
                menuSectionModels.add(toModel(menuSection));
            }
        }
        return menuSectionModels;
    }

    @Override
    public MenuSectionModel updateMenuSection(MenuSectionModel menuSectionModel) {
        return null;
    }

    @Override
    public MenuSectionModel deleteMenuSection(Long menuSectionId) {
        MenuSection menuSection = menuSectionRepository.getById(menuSectionId);

        if(menuSection != null){
            if(menuSection.getIsDeleted()){
                throw new ApiFailException("Menu section is already deleted!!");
            }

            menuSection.setIsDeleted(true);
        }

        menuSectionRepository.save(menuSection);

        return toModel(menuSection);
    }

    private MenuSectionModel toModel(MenuSection menuSection){
        MenuSectionModel menuSectionModel = new MenuSectionModel();
        menuSectionModel.setId(menuSection.getId());
        menuSectionModel.setName(menuSection.getName());
        menuSectionModel.setObjectTypeName(menuSection.getObjectType().getName());
        menuSectionModel.setMenuModels(menuService.getByMenuSection(menuSection));

        return menuSectionModel;
    }
}
