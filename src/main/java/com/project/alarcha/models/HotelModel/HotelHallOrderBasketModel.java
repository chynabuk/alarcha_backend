package com.project.alarcha.models.HotelModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class HotelHallOrderBasketModel extends BaseModel {
    private String hotelName;
    private String name;
    private int hours;
    private float price;
    private float priceForNextHours;
    private float totalPrice;
    private OrderStatus orderStatus;
    private Date createdDate;
    private String img;
}