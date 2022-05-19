package com.project.alarcha.models.AreaModel;

import com.project.alarcha.models.BaseModel;
import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AreaModel extends BaseModel {
//    @NotEmpty(message = "Поле должно быть обязательно заполнено")
    private String areaName;
//    @Email
//    @NotEmpty(message = "Email должен быть заполнен")
    private String email;
    private List<ObjectTypeModel> objectTypeModels;
    private List<HotelModel> hotelModels;
}
