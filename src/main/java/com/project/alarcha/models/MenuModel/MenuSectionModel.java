package com.project.alarcha.models.MenuModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MenuSectionModel extends BaseModel {
    @NotEmpty(message = "Обязательное поле для заполнения")
    private String name;
    private List<MenuModel> menuModels;
    private Long objectTypeId;
    private String objectTypeName;
}
