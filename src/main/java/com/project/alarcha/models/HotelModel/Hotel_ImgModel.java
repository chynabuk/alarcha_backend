package com.project.alarcha.models.HotelModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Hotel_ImgModel extends BaseModel {
    private Long hotelId;
    private String img;
}
