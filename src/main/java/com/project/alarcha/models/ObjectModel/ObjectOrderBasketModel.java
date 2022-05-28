package com.project.alarcha.models.ObjectModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ObjectOrderBasketModel extends BaseModel {
    private String objectType;
    private String name;
    private float price;
    private float priceForNextHours;
    private float totalPrice;
    private long days;
    private int hours;
    private OrderStatus orderStatus;
    private Date createdDate;
    private String img;
}
