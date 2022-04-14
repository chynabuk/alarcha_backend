package com.project.alarcha.models.ObjectModel;

import com.project.alarcha.entities.MenuSection;
import com.project.alarcha.models.BaseModel;
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
    private List<MenuSection> menuSectionModels;
    private List<ObjectModel> objectModels;
    private Long areaId;
}
