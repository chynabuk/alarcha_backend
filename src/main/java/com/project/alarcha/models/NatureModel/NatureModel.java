package com.project.alarcha.models.NatureModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NatureModel extends BaseModel {

    private String name;

    private String description;

    private String img;

    private Long natureTypeId;

    private String type;
}