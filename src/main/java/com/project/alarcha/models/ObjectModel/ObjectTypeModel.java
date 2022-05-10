package com.project.alarcha.models.ObjectModel;

import com.project.alarcha.enums.TimeType;
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
    private Float price;
    private Float pricePerHour;
    private Integer minHours;
    private List<MenuSectionModel> menuSectionModels;
    private List<ObjectModel> objectModels;
    private List<ObjectTypeImgModel> objectTypeImgModels;
    private Long areaId;
    private String areaName;
    private TimeType timeType;
}
