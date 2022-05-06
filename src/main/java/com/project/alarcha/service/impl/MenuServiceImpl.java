package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.MenuModel.MenuModel;
import com.project.alarcha.repositories.MenuRepository;
import com.project.alarcha.repositories.MenuSectionRepository;
import com.project.alarcha.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuSectionRepository menuSectionRepository;

    @Override
    public MenuModel createMenu(MenuModel menuModel) {
        Menu menu = new Menu();
        MenuSection menuSection = menuSectionRepository.getById(menuModel.getMenuSectionId());
        menu.setName(menuModel.getName());
        menu.setPrice(menuModel.getPrice());
        menu.setDescription(menuModel.getDescription());
        menu.setMenuSection(menuSection);
        menu.setIsDeleted(false);

        menuRepository.save(menu);

        return menuModel;
    }

    @Override
    public List<Menu> createMenus(List<MenuModel> menuModels) {
        List<Menu> menus = new ArrayList<>();

        for(MenuModel menuModel : menuModels){
            Menu menu = new Menu();
            menu.setName(menuModel.getName());
            menu.setPrice(menuModel.getPrice());
            menu.setDescription(menuModel.getDescription());
            menu.setIsDeleted(false);

            menus.add(menu);
        }
        return menus;
    }

    @Override
    public List<Menu> convertToMenus(List<MenuModel> menuModels) {
        return null;
    }

    @Override
    public MenuModel getById(Long menuId) {
        Menu menu = menuRepository.getById(menuId);
        return toModel(menu);
    }

    @Override
    public List<MenuModel> getByMenuSection(MenuSection menuSection) {
        List<MenuModel> menuModels = new ArrayList<>();

        for(Menu menu : menuSection.getMenus()){
            if(!menu.getIsDeleted()){
                menuModels.add(toModel(menu));
            }
        }

        return menuModels;
    }

    @Override
    public List<MenuModel> getAll() {
        List<MenuModel> menuModels = new ArrayList<>();

        for(Menu menu : menuRepository.findAll()){
            if(!menu.getIsDeleted()){
                menuModels.add(toModel(menu));
            }
        }

        return menuModels;
    }

    @Override
    public MenuModel updateMenu(MenuModel menuModel) {
        return null;
    }

    @Override
    public MenuModel deleteMenu(Long menuId) {
        Menu menu = menuRepository.getById(menuId);

        if(menu != null){
            if(menu.getIsDeleted()){
                throw new ApiFailException("Menu is already deleted!");
            }

            menu.setIsDeleted(true);
        }

        menuRepository.save(menu);

        return toModel(menu);
    }

    private MenuModel toModel(Menu menu){
        MenuModel menuModel = new MenuModel();
        menuModel.setId(menu.getId());
        menuModel.setName(menu.getName());
        menuModel.setPrice(menu.getPrice());
        menuModel.setDescription(menu.getDescription());
        menuModel.setObjectTypeName(menu.getMenuSection().getObjectType().getName());

        return menuModel;
    }
}
