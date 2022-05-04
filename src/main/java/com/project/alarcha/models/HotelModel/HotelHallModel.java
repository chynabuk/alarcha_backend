package com.project.alarcha.models.HotelModel;

import com.project.alarcha.entities.HotelHallOrder;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelHallModel extends BaseModel {
    private String name;
    private Float price;
    private Float finalPrice;
    private Integer numberOfSeats;
    private String hotelName;
    private Long hotelId;
    private List<HotelHallOrder> hotelHallOrders;

}
