package com.project.alarcha.models.HotelModel;

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
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date registrationDate;
    private Time startTime;
    private Time endTime;
}
