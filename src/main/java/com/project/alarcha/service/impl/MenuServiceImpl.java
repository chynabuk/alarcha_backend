package com.project.alarcha.service.impl;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.models.MenuModel.MenuModel;
import com.project.alarcha.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Override
    public MenuModel createMenu(Menu menu) {
        return null;
    }

    @Override
    public List<Menu> convertToMenus(List<MenuModel> menuModels) {
        List<Menu> menus = new ArrayList<>();

        for(MenuModel menuModel : menuModels){
            Menu menu = new Menu();
            menu.setName(menuModel.getName());
            menu.setPrice(menuModel.getPrice());
            menu.setDescription(menuModel.getDescription());

            menus.add(menu);
        }
        return menus;
    }
}
