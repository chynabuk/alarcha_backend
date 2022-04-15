package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.entities.MenuSection;
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
}
