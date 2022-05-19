package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.MenuModel.MenuModel;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.repositories.MenuSectionRepository;
import com.project.alarcha.repositories.ObjectTypeRepository;
import com.project.alarcha.service.MenuSectionService;
import com.project.alarcha.service.MenuService;
import com.project.alarcha.service.ObjectTypeService;
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

    @Autowired
    private ObjectTypeRepository objectTypeRepository;

    @Override
    public MenuSectionModel createMenuSection(MenuSectionModel menuSectionModel) {
        menuSectionRepository.save(initAndGet(menuSectionModel));
        return menuSectionModel;
    }

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
        MenuSection menuSection = getMenuSection(menuSectionId);

        return toModel(menuSection);
    }

    @Override
    public List<MenuSectionModel> getAll() {
        List<MenuSectionModel> menuSectionModels = new ArrayList<>();

        for(MenuSection menuSection : menuSectionRepository.getAll()){
            menuSectionModels.add(toModel(menuSection));
        }

        return menuSectionModels;
    }

    @Override
    public List<MenuSectionModel> getForSelect() {
        List<MenuSectionModel> menuSectionModels = new ArrayList<>();

        for(MenuSection menuSection : menuSectionRepository.getAll()){
            MenuSectionModel menuSectionModel = new MenuSectionModel();
            menuSectionModel.setId(menuSection.getId());
            menuSectionModel.setName(menuSection.getName());
            menuSectionModels.add(menuSectionModel);
        }
        return menuSectionModels;
    }

    @Override
    public List<MenuSectionModel> getForList() {
        List<MenuSectionModel> menuSectionModels = new ArrayList<>();

        for(MenuSection menuSection : menuSectionRepository.getAll()){
            MenuSectionModel menuSectionModel = new MenuSectionModel();
            menuSectionModel.setId(menuSection.getId());
            menuSectionModel.setName(menuSection.getName());
            menuSectionModels.add(menuSectionModel);
        }
        return menuSectionModels;
    }

    @Override
    public List<MenuSectionModel> getByObjectType(ObjectType objectType) {
        List<MenuSectionModel> menuSectionModels = new ArrayList<>();

        for(MenuSection menuSection : objectType.getMenuSections()){
            if(!menuSection.getIsDeleted()){
                menuSectionModels.add(toModel(menuSection));
            }
        }

        return menuSectionModels;
    }

    @Override
    public MenuSectionModel updateMenuSection(MenuSectionModel menuSectionModel) {
        MenuSection menuSection = getMenuSection(menuSectionModel.getId());

        setValuesOnUpdateMenuSection(menuSection, menuSectionModel);

        menuSectionRepository.save(menuSection);

        return menuSectionModel;
    }

    @Override
    public MenuSectionModel deleteMenuSection(Long menuSectionId) {
        MenuSection menuSection = getMenuSection(menuSectionId);

        for(Menu menu : menuSection.getMenus()){
            if(!menu.getIsDeleted()){
                menu.setIsDeleted(true);
            }
        }

        menuSection.setIsDeleted(true);
        menuSectionRepository.save(menuSection);

        return toModel(menuSection);
    }

    private MenuSection initAndGet(MenuSectionModel menuSectionModel){
        MenuSection menuSection = new MenuSection();
        ObjectType objectType = objectTypeRepository.getById(menuSectionModel.getObjectTypeId());
        menuSection.setName(menuSectionModel.getName());
        menuSection.setObjectType(objectType);
        menuSection.setIsDeleted(false);

        List<MenuModel> menuModels = menuSectionModel.getMenuModels();
        if(menuModels != null){
            menuModels.forEach(menuModel -> menuModel.setObjectTypeName(objectType.getName()));

            List<Menu> menus = menuService.createMenus(menuModels);
            if(menus != null){
                menuSection.setMenus(menus);
                menus.forEach(menu -> menu.setMenuSection(menuSection));
            }
        }

        return menuSection;
    }

    private MenuSection getMenuSection(Long menuSectionId){
        MenuSection menuSection = menuSectionRepository
                .findById(menuSectionId)
                .orElseThrow(() -> new ApiFailException("MenuSection is not found!"));

        if(menuSection.getIsDeleted()){
            throw new ApiFailException("MenuSection is not found or deleted!");
        }

        return menuSection;
    }

    private void setValuesOnUpdateMenuSection(MenuSection menuSection, MenuSectionModel menuSectionModel){
        String name = menuSectionModel.getName();

        if(name != null){
            menuSection.setName(name);
        }
    }

    private MenuSectionModel toModel(MenuSection menuSection){
        MenuSectionModel menuSectionModel = new MenuSectionModel();
        menuSectionModel.setId(menuSection.getId());
        menuSectionModel.setName(menuSection.getName());
        menuSectionModel.setObjectTypeName(menuSection.getObjectType().getName());
        if (!menuSection.getMenus().isEmpty()){
            menuSectionModel.setMenuModels(menuService.getByMenuSection(menuSection));
        }

        return menuSectionModel;
    }
}
