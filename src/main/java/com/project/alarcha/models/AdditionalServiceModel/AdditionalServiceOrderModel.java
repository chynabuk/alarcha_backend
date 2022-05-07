package com.project.alarcha.models.AdditionalServiceModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
public class AdditionalServiceOrderModel extends BaseModel {
    private Long userId;
    private Long additionalServiceId;
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date registrationDate;

    private Time startTime;
    private Time endTime;
}
