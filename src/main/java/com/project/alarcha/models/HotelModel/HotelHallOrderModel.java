package com.project.alarcha.models.HotelModel;

import com.project.alarcha.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class HotelHallOrderModel {
    private Long hotelHallId;
    private Long userId;
    private String userFullName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date startDate;
    private Time startTime;
    private Time endTime;
    private OrderStatus orderStatus;
    private String hotelName;
    private String hotelHallName;
}
