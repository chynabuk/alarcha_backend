package com.project.alarcha.service;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.models.MenuModel.MenuModel;
import com.project.alarcha.models.ObjectModel.ObjectModel;

import java.util.List;

public interface MenuService {
    MenuModel createMenu(MenuModel menuModel);
    List<Menu> createMenus(List<MenuModel> menuModels);
    List<Menu> convertToMenus(List<MenuModel> menuModels);
    MenuModel getById(Long menuId);
    List<MenuModel> getByMenuSection(MenuSection menuSection);
    List<MenuModel> getAll();
    List<MenuModel> getForList();
    MenuModel updateMenu(MenuModel menuModel);
    MenuModel deleteMenu(Long menuId);
}
