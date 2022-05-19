package com.project.alarcha.models.HotelModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelHallModel extends BaseModel {
    @NotEmpty(message = "Обязательное поле для заполнения")
    private String name;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Float price;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Float priceForNextHours;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Integer numberOfSeats;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private String hotelName;
    private Long hotelId;
    private String img;
    private List<HotelHallOrderModel> hotelHallOrders;
    private List<HotelHall_ImgModel> hotelHall_imgModels;

}
