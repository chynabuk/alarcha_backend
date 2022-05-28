package com.project.alarcha.models.ObjectModel;

import com.project.alarcha.enums.OrderStatus;
import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ObjectOrderModel extends BaseModel {
    private Long userId;
    private Long objectId;
    private Float totalPrice;
    private String fullName;
    private String objectName;
    private String objectTypeName;
    private Time startTime;
    private Time endTime;
    private String userPhone;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate endDate;
    private OrderStatus orderStatus;
    private String img;
    private int totalPage;
}
