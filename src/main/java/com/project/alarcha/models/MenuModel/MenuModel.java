package com.project.alarcha.models.MenuModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuModel extends BaseModel {
    private String name;
    private Float price;
    private String description;
    private Long menuSectionId;
    private String objectTypeName;
}
