package com.project.alarcha.models.BlogModel;


import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlogModel extends BaseModel {

    private String title;

    private String description;

    private byte[] photo;
}
