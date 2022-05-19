package com.project.alarcha.models.NatureModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class NatureModel extends BaseModel {

    @NotEmpty(message = "Обязательное поле для заполнения")
    @Length(max = 100, message = "Слишком длинное имя")
    private String name;

    @NotEmpty(message = "Обязательное поле для заполнения")
    private String description;

    private String img;

    private Long natureTypeId;

    private String type;
}