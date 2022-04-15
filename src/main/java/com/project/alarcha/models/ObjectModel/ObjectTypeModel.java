package com.project.alarcha.models.ObjectModel;

import com.project.alarcha.models.BaseModel;
import com.project.alarcha.models.MenuModel.MenuSectionModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ObjectTypeModel extends BaseModel {
    private String name;
    private float price;
    private List<MenuSectionModel> menuSectionModels;
    private List<ObjectModel> objectModels;
    private Long areaId;
    private String areaName;
}
