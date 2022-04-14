package com.project.alarcha.models.MenuModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MenuSectionModel extends BaseModel {
    private String name;
    private List<MenuModel> menuModels;
}
