package com.project.alarcha.models.HotelModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelHall_ImgModel extends BaseModel {
    private Long hotelHallId;
    private String img;
}
