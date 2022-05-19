package com.project.alarcha.models.MenuModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class MenuModel extends BaseModel {
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private String name;
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private Float price;
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private String description;
    private Long menuSectionId;
    private String objectTypeName;
}
