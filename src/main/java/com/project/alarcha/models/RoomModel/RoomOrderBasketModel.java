package com.project.alarcha.models.RoomModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomOrderBasketModel extends BaseModel {
    private String hotelName;
    private String roomType;
    private float price;
    private float totalPrice;
    private int days;
    private int roomNumber;
    private String img;
    private OrderStatus orderStatus;
}
