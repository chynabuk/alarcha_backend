package com.project.alarcha.models.ObjectModel;

import com.project.alarcha.entities.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ObjectModel extends BaseEntity {
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private String name;
//    @NotEmpty(message = "Обязательное поле для заполнения")
    private Integer numberOfSeats;
    private Long objectTypeId;
    private String objectTypeName;
    private List<ObjectOrderModel> objectOrderModels;
}
