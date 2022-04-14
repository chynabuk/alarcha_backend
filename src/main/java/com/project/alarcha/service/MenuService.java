package com.project.alarcha.service;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.models.MenuModel.MenuModel;

import java.util.List;

public interface MenuService {
    MenuModel createMenu(Menu menu);
    List<Menu> convertToMenus(List<MenuModel> menuModels);
}
