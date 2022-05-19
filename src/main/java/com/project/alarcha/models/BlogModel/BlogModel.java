package com.project.alarcha.models.BlogModel;


import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class BlogModel extends BaseModel {

    @NotEmpty(message = "Поле должно быть заполнено")
    private String title;
    @NotEmpty(message = "Поле должно быть заполнено")
    private String description;

    private byte[] photo;
}
