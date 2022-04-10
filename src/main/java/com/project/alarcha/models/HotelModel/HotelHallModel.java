package com.project.alarcha.models.HotelModel;

import com.project.alarcha.entities.HotelHallOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelHallModel {
    private String name;
    private Float price;
    private List<HotelHallOrder> hotelHallOrders;

}
