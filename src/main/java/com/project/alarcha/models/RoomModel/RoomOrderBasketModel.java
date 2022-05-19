package com.project.alarcha.models.RoomModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private OrderStatus orderStatus;
    private Date createdDate;
    private String img;
}
