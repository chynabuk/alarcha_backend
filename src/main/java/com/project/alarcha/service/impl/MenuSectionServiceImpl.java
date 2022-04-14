package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import com.project.alarcha.repositories.MenuSectionRepository;
import com.project.alarcha.service.MenuSectionService;
import com.project.alarcha.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuSectionServiceImpl implements MenuSectionService {
    @Autowired
    private MenuSectionRepository menuSectionRepository;

    @Autowired
    private MenuService menuService;

    @Override
    public MenuSectionModel createMenuSection(MenuSectionModel menuSectionModel) {
        MenuSection menuSection = new MenuSection();

        menuSectionRepository.save(initAndGet(menuSection, menuSectionModel));

        return menuSectionModel;
    }

    private MenuSection initAndGet(MenuSection menuSection, MenuSectionModel menuSectionModel){
        menuSection.setName(menuSectionModel.getName());
        menuSection.setMenus(
                menuService.convertToMenus(menuSectionModel.getMenuModels())
        );

        for(Menu menu : menuSection.getMenus()){
            menu.setMenuSection(menuSection);
        }

        return menuSection;
    }
}
