package com.project.alarcha.service;

import com.project.alarcha.entities.Menu;
import com.project.alarcha.models.MenuModel.MenuModel;

import java.util.List;

public interface MenuService {
    List<Menu> createMenus(List<MenuModel> menuModels);
    List<Menu> convertToMenus(List<MenuModel> menuModels);
}
