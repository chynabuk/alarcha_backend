package com.project.alarcha.models.RoomModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RoomOrderModel extends BaseModel {
    private Long roomId;
    private Long userId;
    private String userFullName;
    private int roomNumber;
    private String roomType;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date endDate;
    private OrderStatus orderStatus;
    private String hotelName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date expirationDate;
    private String userPhone;
    private Float totalPrice;
    private String img;
    private int totalPage;
}
