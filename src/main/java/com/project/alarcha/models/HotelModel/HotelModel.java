package com.project.alarcha.models.HotelModel;

import com.project.alarcha.models.BaseModel;
import com.project.alarcha.models.RoomModel.RoomTypeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelModel extends BaseModel {
    @NotEmpty(message = "Обязательное поле для заполнения")
    private String hotelName;
    private String imgUrl;
    private List<RoomTypeModel> roomTypeModels;
    private List<HotelHallModel> hotelHallModels;
    private List<Hotel_ImgModel> hotelImgModels;
    private Long areaId;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private String areaName;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Float minPrice;
}