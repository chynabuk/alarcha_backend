package com.project.alarcha.models.HotelModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class HotelHallOrderModel extends BaseModel {
    private Long hotelHallId;
    private Long userId;
    private String userFullName;
    @NotEmpty(message = "Обязательное поле для заполнения")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate startDate;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Time startTime;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Time endTime;
    private OrderStatus orderStatus;
    private String hotelName;
    private String hotelHallName;
    private String userPhone;
    private Float totalPrice;
    private String img;
    private int totalPage;
}
