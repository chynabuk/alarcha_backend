package com.project.alarcha.models.AreaModel;

import com.project.alarcha.entities.Hotel;
import com.project.alarcha.entities.Object;
import com.project.alarcha.entities.ObjectType;
import com.project.alarcha.models.BaseModel;
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
    private List<ObjectType> objectTypes;
    private List<Hotel> hotels;
}
