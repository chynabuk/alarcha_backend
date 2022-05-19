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
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class HotelHallOrderModel extends BaseModel {
    private Long hotelHallId;
    private Long userId;
    @NotEmpty(message = "Обязательное поле для заполнения")
    @Length(max = 100, message = "Слишком длинное поле")
    private String userFullName;
    @NotEmpty(message = "Обязательное поле для заполнения")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date startDate;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Time startTime;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private Time endTime;
    private OrderStatus orderStatus;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private String hotelName;
    @NotEmpty(message = "Обязательное поле для заполнения")
    private String hotelHallName;
    @NotEmpty(message = "Обязательное поле для заполнения")
    @Pattern(regexp = "^\\+\\d+ \\d+$", message = "Неправильной формат номера телефона")
    private String userPhone;
    private Float totalPrice;
    private String img;
    private int totalPage;
}
