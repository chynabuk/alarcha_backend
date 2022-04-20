package com.project.alarcha.models.AreaModel;

import com.project.alarcha.models.BaseModel;
import com.project.alarcha.models.HotelModel.HotelModel;
import com.project.alarcha.models.ObjectModel.ObjectTypeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AreaModel extends BaseModel {
    private String areaName;
    private String email;
    private List<ObjectTypeModel> objectTypeModels;
    private List<HotelModel> hotelModels;
}
