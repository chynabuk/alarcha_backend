package com.project.alarcha.models.AdditionalServiceModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdditionalServiceModel extends BaseModel {

    private String name;

    private String description;

    private Float price;

    private Float priceForNextHours;

    private Integer inStock;
}
