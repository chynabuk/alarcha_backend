package com.project.alarcha.models.RoomModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RoomOrderModel extends BaseModel {
    private Long roomId;
    private Long userId;
//    @NotEmpty(message = "Обязательное для заполнение поле")
    private String userFullName;
    private int roomNumber;
    private String roomType;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate endDate;
    private OrderStatus orderStatus;
    private String hotelName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate expirationDate;
//    @Pattern(regexp = "^\\+\\d+ \\d+$", message = "Неправильный формат номера телефона")
    private String userPhone;
    private Float totalPrice;
    private String img;
    private int totalPage;
}
