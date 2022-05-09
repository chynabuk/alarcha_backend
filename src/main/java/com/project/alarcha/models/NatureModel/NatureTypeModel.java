package com.project.alarcha.models.NatureModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NatureTypeModel extends BaseModel {
    private String type;
    private List<NatureModel> natureModels;
}
